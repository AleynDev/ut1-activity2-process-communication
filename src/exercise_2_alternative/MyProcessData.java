package exercise_2_alternative;

import java.util.ArrayList;

public class MyProcessData {

    private final String imageName;
    private final String pid;
    private final ArrayList<String> services;

    public MyProcessData(String imageName, String pid, ArrayList<String> services) {
        this.imageName = imageName;
        this.pid = pid;
        this.services = services;
    }

    public String getImageName() {
        return imageName;
    }

    public String getPid() {
        return pid;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    @Override
    public String toString() {
        return "ProcessData{" +
                "imageName='" + imageName + '\'' +
                ", pid='" + pid + '\'' +
                ", services='" + services + '\'' +
                '}';
    }
}
