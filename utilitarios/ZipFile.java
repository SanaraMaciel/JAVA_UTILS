

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFile {

    public static File unzipShapeFile(String zipFilePath) {
        File shapeFile = null;
        String destDir = zipFilePath;
        if (zipFilePath.contains(".")) {
            destDir = destDir.split("\\.")[0];
        }
        File dir = new File(destDir);
        // Cria um diretorio se o mesmo não existir
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer para ler e escrever um file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+ newFile.getAbsolutePath());
                //criar diretorios para sub-diretorios
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();

                if (fileName.contains(".shp")) {
                    shapeFile = newFile;
                }
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shapeFile;

    }

    public static List<File> unzip(String zipFilePath) {
        List<File> files = new ArrayList<>();
        String destDir = zipFilePath;
        if (zipFilePath.contains(".")) {
            destDir = destDir.split("\\.")[0];
        }
        File dir = new File(destDir);
        // Cria um diretorio se o mesmo não existir
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer para ler e escrever um file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+ newFile.getAbsolutePath());
                files.add(newFile);
                //criar diretorios para sub-diretorios
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;

    }

}
