package exercise_1;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConcurrentProcesses {

    public static void main(String[] args) {

        /**
         * 1) Escribe un programa en Java que lance un proceso que escriba bytes desde el proceso padre
         * hacia el proceso hijo y recoge los bytes y los pasa a mayúsculas y CONCURRENTEMENTE lanza un
         * proceso que ejecute el reproductor de música wmplayer.exe reproduciendo una canción.
         */

        try {
            Runtime runTextToUppercase = Runtime.getRuntime();
            Process textToUppercase = runTextToUppercase.exec("java -jar ./src/exercise_1/ConvertToUppercase.jar");

            // Preguntar a Rita si existe alguna diferencia entre lanzarlo de una manera u otra
            //Runtime runWmplayer = Runtime.getRuntime();
            //Process wmplayer = runWmplayer.exec("cmd /C start wmplayer \"C:\\Users\\Aleyn\\Videos\\Videos PGL\\UT2_Actividad_1.mp4\"");
            Runtime.getRuntime().exec("cmd /C start wmplayer \"C:\\Users\\Aleyn\\Videos\\Videos PGL\\UT2_Actividad_1.mp4\"");

            OutputStream os = textToUppercase.getOutputStream();
            os.write(("texto en minusculas escrito desde el padre" + "\n").getBytes(StandardCharsets.UTF_8));
            os.flush();

            InputStream is = textToUppercase.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
