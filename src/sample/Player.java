package sample;

public class Player {
    public String name, ip, score;

    public Player(String name, String ip) {
        this.name = name;
        this.ip = ip;
        this.score = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return name + " " + score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score){
        this.score = score;
    }
}
