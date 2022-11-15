import java.awt.*;

public class Bullet {   // 총알 위치 파악 및 이동 위한 클래스
    int x;
    int y;
    Bullet(int x, int y) {
        this.x = x;
        this.y = y; // 미사일 좌표를 체크
    }

    public void move() {
        x += 20;
    }
}
