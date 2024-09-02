

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cpqd.pcri.apiadm.dto.PointDTO;
import br.com.cpqd.pcri.apiadm.dto.PolygonCompareDTO;
import br.com.cpqd.pcri.apiadm.utils.shp.ShpReader;

public class PolygonUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PolygonUtils.class);	
	/* Função que cria um objeto do tipo geomtria apartir de um geojson */
	public static Geometry fromJson(String coordinates) {
		com.mapbox.geojson.Polygon polygon = com.mapbox.geojson.Polygon.fromJson(coordinates);
		List<Coordinate> coordinateList = new ArrayList<>();
		polygon.coordinates().forEach(list -> list.forEach(
				point -> coordinateList.add(new Coordinate(point.longitude(), point.latitude(), point.altitude()))));
		Coordinate[] arr = coordinateList.toArray(new Coordinate[0]);
		GeometryFactory geometryFactory = new GeometryFactory();
		Geometry geometry = geometryFactory.createPolygon(arr);
//        geometry.setSRID(4674);
		return geometry;
	}

	/*
	 * Função que cria uma String do tipo Geojson apartir de um objeto do tipo
	 * geometria
	 */
	public static String toJson(Geometry geometry) {
		List<com.mapbox.geojson.Point> points = Arrays.stream(geometry.getCoordinates()).map(coordinate -> {
			return com.mapbox.geojson.Point.fromLngLat(coordinate.getX(), coordinate.getY(), coordinate.getZ());
		}).collect(Collectors.toList());
		return com.mapbox.geojson.Polygon.fromLngLats(Arrays.asList(points)).toJson();
	}

	/*
	 * Funcão que cria uma lista de objetos do tipo Ponto apartir de um objeto do
	 * tipo geometria
	 */
	public static List<PointDTO> toPoints(Geometry geometry) {
		return Arrays.stream(geometry.getCoordinates()).map(coordinate -> {
			return new PointDTO(coordinate.getX(), coordinate.getY(), coordinate.getZ());
		}).collect(Collectors.toList());
	}
	// x : Latitude

	/*
	 * função que valida se uma geometria esta contida em outra geometria com margem
	 * de 5% de erro
	 */
	public static boolean ifGeometryAInsideGeometryB(PolygonCompareDTO polygonCompareDTO) {
		Geometry a = fromJson(polygonCompareDTO.getA().getGeoJson());
		Geometry b = fromJson(polygonCompareDTO.getB().getGeoJson());
		if (!b.contains(a)) {
			/*
			 * se a geometria da area não estiver totalmente dentro da area propriedade: 1 -
			 * calculo a geometria da parte que não esta contida na area da propriedade; 2 -
			 * calculo a area desta geometria e area da geometria que esta sendo analisada;
			 * 3 - calculo o percentual da diferenca entre elas. Tolerancia é de ate 5% de
			 * diferença
			 */
			double areaDiff = (a.difference(b)).getArea();
			double area = a.getArea();
			double percentual = (areaDiff / area) * 100;
			return percentual <= 5;
		}
		return true;
	}
	
	/* Função que converte o sistema de coordenadas para 4326 caso necessário*/
	public static Geometry transformToSRC4326(Geometry geom, CoordinateReferenceSystem sourceCRS) {
		MathTransform transform;
		Geometry geom4326 = new GeometryFactory().createEmpty(2);
		try {
			CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
		    if(sourceCRS.getCoordinateSystem().getName().equals(targetCRS.getCoordinateSystem().getName())) {
		    	return geom;
		    }			
			transform = CRS.findMathTransform(sourceCRS, targetCRS);
			geom4326 = JTS.transform(geom,transform);
		} catch (FactoryException | MismatchedDimensionException | TransformException e) {
			LOGGER.info("Erro na transformação do sistema de coordenadas", e);
		}
		return geom4326;
	}
	
	/* Funçao que cria uma geometria apartir de um shapefile */
	public static Geometry shapeFileToGeometry(File file) throws IOException {

		File shapeFile = ZipFile.unzipShapeFile(file.getAbsolutePath());

		ShapefileDataStore shpdataStore = new ShapefileDataStore(shapeFile.toURI().toURL());
	    ContentFeatureSource featureSource = shpdataStore.getFeatureSource();
	    ContentFeatureCollection collection = featureSource.getFeatures();
	    SimpleFeatureType schema = featureSource.getSchema();
	    CoordinateReferenceSystem sourceCRS = schema.getCoordinateReferenceSystem();

	/*	Este código faz a mesma função do codigo acima, le um shapefile e converte para featurecolletion
	 * 
		Map<String, Object> map = new HashMap<>();
		map.put("url", file.toURI().toURL());	    
		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
		*/
	    
		Polygon polygon = new GeometryFactory().createPolygon();
		try (FeatureIterator<SimpleFeature> features = collection.features()) {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				MultiPolygon multiPolygon = (MultiPolygon) feature.getDefaultGeometry();
				polygon = (Polygon) multiPolygon.getGeometryN(0);
//				polygon = (Polygon) transformToSRC4326(multiPolygon.getGeometryN(0), sourceCRS);
//        	    Geometry geometry = (Geometry) feature.getDefaultGeometry();
			}
		}
		return polygon.getGeometryN(0);
		
	}

	/* Funçao que cria uma geometria apartir de um shapefile */
	public static Geometry shapeFileForPolygon(File file) throws IOException {
		List<Coordinate> coordinateList = new ArrayList<>();
		final FileInputStream fileInputStream = new FileInputStream(file);
		try {
			final com.esri.core.geometry.Polygon polygon = new com.esri.core.geometry.Polygon();
			final ShpReader shpReader = new ShpReader(new DataInputStream(new BufferedInputStream(fileInputStream)));
			while (shpReader.hasMore()) {
				shpReader.queryPolygon(polygon);
				for (int x = 0; x < polygon.getPointCount(); x++) {
					com.esri.core.geometry.Point point = polygon.getPoint(x);
					coordinateList.add(new Coordinate(point.getX(), point.getY(), point.getZ()));
				}
			}
		} finally {
			fileInputStream.close();
		}

		Coordinate[] arr = coordinateList.toArray(new Coordinate[0]);
		GeometryFactory geometryFactory = new GeometryFactory();
		Geometry geometry = geometryFactory.createPolygon(arr);
//        geometry.setSRID(4674);
		return geometry;
	}

}
