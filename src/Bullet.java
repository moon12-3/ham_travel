import java.awt.*;

public class Bullet {   // 총알 위치 파악 및 이동 위한 클래스
    int x;
    int y;
    //int angle;
    int speed;

    int who;

    Bullet(int x, int y, int speed, int who) {
        this.x = x;
        this.y = y; // 미사일 좌표를 체크
        this.speed = speed;
        this.who = who;
    }

    public void move() {
        x += speed;
    }
}
