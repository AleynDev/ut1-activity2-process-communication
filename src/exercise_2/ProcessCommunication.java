package exercise_2;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ProcessCommunication {

    /**
     * 2)  Escribe un programa en Java que lance un proceso que ejecute un comando del sistema para
     * saber qué servicios se están ejecutando bajo svchost.exe (servicios de Windows). Para ello sigue
     * los siguientes pasos:
     *
     * El proceso hijo ejecuta el comando: tasklist /SVC /FO LIST
     * (se recomienda ver el significado de los parámetros solicitados y cómo se devolverá la información
     * con tasklist /?)
     *
     * El hijo le enviará al padre el listado de los servicios.
     *
     * El proceso padre lee la lista de servicios que le envía el hijo pero sólo muestra por pantalla la
     * información referida a los que dependen de svchost.exe (para facilitar la tarea, si un mismo PID
     * tuviera varias denominaciones sólo coge el último).
     *
     * Utiliza un fichero bat u otro programa en java (recomiendo practicar de las dos maneras ;-)
     * que sea el que lance el comando y al que deberás enviar el nombre del servicio con
     * OutputStream: El proceso hijo ejecuta el comando
     *
     *              "sc qc nombre_servicio"
     *
     * para leer la configuración referida a ese servicio;
     *
     * por ejemplo: sc qc PolicyAgent y el hijo le enviará al padre la salida del comando.
     */

    public static void main(String[] args) {

        String taskName = "svchost.exe";

        try {

            Runtime runTasklist = Runtime.getRuntime();
            Process tasklist = runTasklist.exec("tasklist /SVC /FO LIST");

            // Leer
            InputStream is = tasklist.getInputStream();
            BufferedReader brTask = new BufferedReader(new InputStreamReader(is));

            infoProcess(brTask, taskName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void infoServiceBat(String serviceName) {
        Runtime runInfoServiceJar = Runtime.getRuntime();
        try {
            Process infoServiceJar = runInfoServiceJar.exec("./src/exercise_2/infoservice.bat");
            // Escribir
            OutputStream os = infoServiceJar.getOutputStream();
            os.write((serviceName).getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
            // Leer
            InputStream is = infoServiceJar.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println("  --->" + line);
            }
            bf.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void infoService(String serviceName) {
        Runtime runInfoServiceJar = Runtime.getRuntime();
        try {
            Process infoServiceJar = runInfoServiceJar.exec("java -jar ./src/exercise_2/InfoService.jar");
            // Escribir flujo de salida
            OutputStream os = infoServiceJar.getOutputStream();
            os.write((serviceName).getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
            // Leer flujo de entrada
            InputStream is = infoServiceJar.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
            }
            bf.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void infoProcess(BufferedReader br, String task) {

        try {
            // Variables que necesito para tratar los datos de br
            String line;
            int i = 0;
            String imageName = "";
            String pid = "";
            String services = "";
            boolean check = false;

            // Comienzo a leer linea a linea el br...
            while ((line = br.readLine()) != null) {
                // Variable data -> separo la información de cada dato mediante los ":"
                String data = line.substring(line.indexOf(':') + 1).trim();
                if ((i == 1) && (line.contains(task))) {
                    check = true;
                    imageName = data;
                    System.out.println("\nImage Name: " + imageName);
                } else if ((i == 2) && check) {
                    pid = data;
                    System.out.println("PID: " + pid);
                } else if ((i >= 3) && check) {
                    services = data;
                    if (services.length() > 1) {
                        System.out.println("Services: " + services);
                        //infoService(services);  // con .jar
                        infoServiceBat(services);  // con .bat
                    }
                }
                // Empieza la lectura con una línea vacía y separa los registros con
                // una línea vacía --> (length = 0)
                if (line.length() != 0) {
                    i++;
                } else {
                    // Pongo check en false e i = 1 cuando -> line.length() == 0, para q vuelva a evaluar
                    // si el nuevo registro es de tipo svchost.exe
                    check = false;
                    i = 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
