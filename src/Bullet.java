import java.awt.*;

public class Bullet {   // 총알 위치 파악 및 이동 위한 클래스
    Point pos;  // 미사일 좌표
    Bullet(int x, int y) {
        pos = new Point(x, y);  // 미사일 좌표를 체크
    }

    public void move() {
        pos.x += 20;
    }
}
