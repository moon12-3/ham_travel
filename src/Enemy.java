public class Enemy {    // 적 위치 파악 및 이동
    int x;
    int y;

    Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x-= 5;
    }
}