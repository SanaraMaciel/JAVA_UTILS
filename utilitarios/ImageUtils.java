

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtils {

    private ImageUtils() {
    }

    public static byte[] generateThumbnail(byte[] imagemOriginal, String extension) throws InterruptedException, IOException {
        Image imagem = Toolkit.getDefaultToolkit().createImage(imagemOriginal);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(imagem, 0);
        mediaTracker.waitForID(0);

        // define a largura e altura do thumbnail
        int largura = 150;
        int altura = 100;
        double thumbRatio = (double) largura / (double) altura;
        int larguraImagem = imagem.getWidth(null);
        int alturaImagem = imagem.getHeight(null);
        double imageRatio = (double) larguraImagem / (double) alturaImagem;
        if (thumbRatio < imageRatio) {
            altura = (int) (largura / imageRatio);
        } else {
            largura = (int) (altura * imageRatio);
        }
        // Desenha a imagem original para o thumbnail e redimensiona para o novo tamanho
        BufferedImage thumbImage = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(imagem, 0, 0, largura, altura, null);

        // Salva a nova imagem
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(thumbImage, extension, out);

        byte[] imagemThumb = out.toByteArray();
        out.close();
        return imagemThumb;
    }

    public static BufferedImage resizeImage(BufferedImage bufferedImage, int width, int height) {
        int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    public static byte[] compressImage(BufferedImage bufferedImage, int quality) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressImage(bufferedImage, baos, quality);
        return baos.toByteArray();
    }

    public static void compressImage(BufferedImage bufferedImage, File outfile, int quality) throws IOException {
        compressImage(bufferedImage, new FileOutputStream(outfile), quality);
    }


    public static void compressImage(BufferedImage bufferedImage, OutputStream output, int quality) throws IOException {
        if (quality <= 0 || quality > 100) {
            throw new IllegalArgumentException("quality not in 1-100");
        }
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        try {
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(quality * 0.01f);
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(output)) {
                jpgWriter.setOutput(ios);
                IIOImage outputImage = new IIOImage(bufferedImage, null, null);
                jpgWriter.write(null, outputImage, jpgWriteParam);
            }
        } finally {
            jpgWriter.dispose();
        }
    }
}
