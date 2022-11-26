import java.awt.*;

public class BossEnemy extends Enemy{
    int cnt;
    int mode;
    int a = -1;
    BossEnemy(int level, int x, int y) {
        super(x, y, 4);
        hp = 500+300*level;
        mode = 4;
        cnt = 1;
    }

    public void move() {
        if(x>600) {
            x-=5;
        }
        cnt++;

        if(cnt%300==0) {
            cnt = 1;
            a++;
            mode = a%4;
        }

        switch (mode){
            case 4 :
                break;
            case 0 :    // 위로 이동
            case 1 :
                if(y>60) y-=3;
                if(cnt%60==0) mode=4;
                break;
            case 2 :    // 아래로 이동
            case 3 :
                if(y<500) y+=3;
                if(cnt%60==0) mode=4;
                break;
        }
    }
}
//Enemy en=new Enemy(this, 1, gScreenWidth*100, 24000, 1, 0);// img 값이 1, kind 값이 1
// drawImageAnc(enemy[1], buff.dis.x, buff.dis.y, 4);//보스 출력

