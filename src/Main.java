
import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String args[]) {
        new Frame_make();
    }
}

class Frame_make extends JFrame implements KeyListener, Runnable{
    int width;
    int height;
    int cnt;
    int eCnt;
    int iCnt = 0;
    int hpCnt = 0;
    int hppCnt = 0;
    int fire_speed;

    int x, y; // 캐릭터의 좌표 변수
    int[] cx = {0, 0, 0};
    int hp;    // 캐릭터 피
    int gameScore;
    int bx = -3; // 배경 스크롤 변수
    int bx2 = 2107;
    boolean KeyUp = false; //키보드 입력 처리를 위한 변수
    boolean KeyDown = false;
    boolean KeyLeft = false;
    boolean KeyRight = false;
    boolean KeySpace = false;

    boolean isDamaged = false;
    boolean isHealed = false;

    Thread th;  // 스레드 생성

    Toolkit tk = Toolkit.getDefaultToolkit();   // 이미지 불러오는 툴킷
    Image[] player;
    int playerStatus = 0;
    Image[] item_img;   // 아이템 이미지
    Image bullet;
    Image enemy;
    Image backGround1;
    Image[] Cloud_img; // 구름
    Image[] Explo_img;  // 폭발이펙트용
    ArrayList bulletList = new ArrayList();
    ArrayList enemyList = new ArrayList();
    ArrayList itemList = new ArrayList();
    ArrayList eBulletList = new ArrayList();
    ArrayList explosionList = new ArrayList();
    Image buffImage;    // 더블 버퍼링용
    Graphics buffg;     // 더블 버퍼링용 2

    Bullet bu;
    Enemy en;
    Item it;
    EnemyBullet eb;
    Explosion ex;

    Frame_make() {
        super("햄모험");
        init();
        setSize(width, height);
        start();
        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);
        setVisible(true);
        setResizable(false);
    }

    public void init() { // 컴포넌트 세팅
        x = 100; //캐릭터의 최초 좌표.
        y = 100;
        hp = 3;    // 초기 캐릭터 생명 (하트 3개)
        width = 1200;
        height = 800;

        backGround1 = new ImageIcon("src/img/background1.png").getImage();
        player = new Image[2];
        player[0] = new ImageIcon("src/img/player.png").getImage();
        player[1] = new ImageIcon("src/img/playerDamaged.png").getImage();
        bullet = new ImageIcon("src/img/bullet.png").getImage();
        enemy = new ImageIcon("src/img/enemy2.png").getImage();

        Cloud_img = new Image[3];
        for(int i = 0; i < Cloud_img.length; i++) {
            Cloud_img[i] = new ImageIcon("src/img/cloud/cloud"+i+".png").getImage();
        }

        item_img = new Image[2];
        for(int i = 0; i < item_img.length; i++) {
            item_img[i] = new ImageIcon("src/img/item/item_"+i+".png").getImage();
        }

        Explo_img = new Image[9];
        for(int i = 0; i < Explo_img.length; i++) {
            Image img = new ImageIcon("src/img/explosion/explosion_00"+(i+1)+".png").getImage();
            Explo_img[i] = img.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        }

        gameScore = 0;  // 게임 점수
        fire_speed = 15; //총알 속도
    }

    public void start() { // 시작처리명령
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(this);    //키보드 이벤트 실행
        th = new Thread(this);  // 스레드 생성
        th.start();
    }

    @Override
    public void run() {
        try{ // 예외옵션 설정으로 에러 방지

            while(true){ // while 문으로 무한 루프 시키기
                KeyProcess(); // 키보드 입력처리를 하여 x,y 갱신
                repaint(); // 갱신된 x,y값으로 이미지 새로 그리기
                Thread.sleep(20); // 20 milli sec 로 스레드 돌리기
                cnt++;
                iCnt++;
                eCnt++;
                if(isDamaged) {
                    hpCnt++;
                    playerStatus = 1;
                }
                if(hpCnt == 60) {
                    hpCnt = 0;
                    isDamaged=false;
                }
                if(isHealed) {
                    hppCnt++;
                    playerStatus = 1;
                }
                if(hppCnt == 60) {
                    hppCnt = 0;
                    isHealed=false;
                }
                EnemyProcess();
                itemProcess();
                BulletProcess();
                ExplosionProcess();
                // eBulletProcess();

                bx-=2;
                bx2-=2;

                if(bx < -(backGround1.getWidth(null))) {
                    bx = backGround1.getWidth(null);
                }
                if(bx2 < -(backGround1.getWidth(null))) {
                    bx2 = backGround1.getWidth(null);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void EnemyProcess() {
        for(int i = 0; i < enemyList.size(); i++) {
            en = (Enemy) enemyList.get(i);
            en.move();
            if(en.x < -200) enemyList.remove(i);

            if(Crash(x, y, en.x, en.y, player[0], enemy, 2)) {
                isDamaged = true;
                if(hpCnt == 1) {
                    hp--;
                    bullet = new ImageIcon("src/img/bullet.png").getImage();
                }
            }
        }

        if((eCnt % 80) == 0) {
            enemyList.add(new Enemy(width+25, (int)(Math.random()*621)+30));

        }
    }

    public void itemProcess() {
        for(int i = 0; i < itemList.size(); i++) {
            it = (Item)itemList.get(i);
            it.move();

            if(it.x < -170) itemList.remove(i);

            if(Crash(x, y, it.x, it.y, item_img[0], player[0],1)) {
                if(it.type%2==0) {
                    isDamaged = true;
                    if (hpCnt == 1) {
                        hp--;
                        bullet = new ImageIcon("src/img/bullet.png").getImage();
                        itemList.remove(i);
                        gameScore -= 10;
                    }
                }
                else {
                    isHealed = true;
                    hp++;
                    bullet = new ImageIcon("src/img/bullet2.png").getImage();
                    itemList.remove(i);
                    gameScore+=10;
                }
            }
        }
        if((iCnt % 100) == 0) {
            itemList.add(new Item(width+25, (int)(Math.random()*621)+30));
        }
    }

    public void BulletProcess() {
        if(KeySpace) {
            if(cnt%fire_speed==0) {
                bu = new Bullet(x+50, y+50);
                bulletList.add(bu);
            }
        }
        else cnt = 14;
        for(int i = 0; i < bulletList.size(); i++) {
            bu = (Bullet)bulletList.get(i);
            bu.move();
            if(bu.x > width - 20) {
                bulletList.remove(i);
            }
            for(int j = 0; j < enemyList.size(); j++) {
                en = (Enemy) enemyList.get(j);
                if(Crash(bu.x, bu.y, en.x, en.y, bullet, enemy, 1)) {
                    bulletList.remove(i);
                    ((Enemy) enemyList.get(j)).hp -= 5;
                }
                if( ((Enemy) enemyList.get(j)).hp <= 0) {
                    enemyList.remove(j);
                    gameScore+=10;
                    ex = new Explosion(en.x + enemy.getWidth(null)/2, en.y + enemy.getHeight(null)/2, 0);
                    explosionList.add(ex);
                }
            }
        }

    }

    public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2, int c){
        boolean check = false;
        if ( Math.abs( ( x1 + img1.getWidth(null) / 2 )
                - ( x2 + img2.getWidth(null) / c ))
                < ( img2.getWidth(null) / 2 + img1.getWidth(null) / 2 )
                && Math.abs( ( y1 + img1.getHeight(null) / 2 )
                - ( y2 + img2.getHeight(null) / 2 ))
                < ( img2.getHeight(null)/2 + img1.getHeight(null)/2 ) ) //이미지 넓이, 높이값을 바로 받아 계산
            check = true;//위 값이 true면 check에 true를 전달

        return check;
    }

    public void ExplosionProcess() {
        for(int i= 0; i < explosionList.size(); i++) {
            ex = (Explosion)explosionList.get(i);
            ex.effect();
        }
    }

    public void eBulletProcess() {
        if(cnt % 50 == 0) {
            eb = new EnemyBullet(en.x - 79, en.y + 35);
            eBulletList.add(eb);
        }
    }

    @Override
    public void paint(Graphics g) {
        buffImage = createImage(width, height); //화면크기와 동일
        buffg = buffImage.getGraphics();    // 버퍼의 그래픽 객체를 얻기
        update(g);
//        g.clearRect(0, 0, width, height); // 0,0 에서 위에서 정한 해상도 크기만큼 화면을 지움
//        g.drawImage(player, x, y, this); // (x, y)에 player 이미지를 그려넣음

    }

    public void update(Graphics g) {
        Draw_Background(); //그려진 배경 가져오기

        Draw_Item();

        Draw_Player();

        Draw_Missile(); // 그려진 총알 가져오기
        Draw_Enemy();   // 그려진 적 가져오기

        Draw_Explosion();
        Draw_StatusText();

        g.drawImage(buffImage, 0, 0, this); // 화면에 버퍼에 그린 그림을 가져와 그리기
    }

    public void Draw_StatusText() {
        buffg.setFont(new Font("Default", Font.BOLD, 20));

        buffg.drawString("SCORE : " + gameScore, 1000, 70);
        buffg.drawString("HP " + hp, 1000, 90);
        buffg.drawString("Enemy Count : " + enemyList.size(), 1000, 110);
    }

    public void Draw_Enemy() {
        for(int i = 0; i < enemyList.size(); ++i) {
            en = (Enemy)enemyList.get(i);
            buffg.drawImage(enemy, en.x, en.y, this);
        }
    }

    public void Draw_Item() {
        for(int i = 0; i< itemList.size(); i++) {
            it = (Item)itemList.get(i);
            buffg.drawImage(item_img[it.type%2], it.x, it.y, this);
        }
    }

    public void drawExplo(int idx, Explosion ex) {
        buffg.drawImage( Explo_img[idx], ex.x -
                Explo_img[idx].getWidth(null) / 2, ex.y -
                Explo_img[idx].getHeight(null) / 2, this);
    }

    public void Draw_Explosion() {
        for(int i = 0; i < explosionList.size(); i++) {
            ex=(Explosion)explosionList.get(i);
            if(ex.damage == 0) {
                if(ex.ex_cnt < 5) {
                    drawExplo(0, ex);
                }else if ( ex.ex_cnt < 10 ) {
                    drawExplo(1, ex);
                }else if ( ex.ex_cnt < 15 ) {
                    drawExplo(2, ex);
                }else if ( ex.ex_cnt < 20 ) {
                    drawExplo(3, ex);
                }else if ( ex.ex_cnt < 25 ) {
                    drawExplo(4, ex);
                }else if ( ex.ex_cnt < 30 ) {
                    drawExplo(5, ex);
                }else if ( ex.ex_cnt < 35 ) {
                    drawExplo(6, ex);
                }else if ( ex.ex_cnt < 40 ) {
                    drawExplo(7, ex);
                }else if ( ex.ex_cnt < 45 ) {
                    drawExplo(8, ex);
                } else if( ex.ex_cnt > 50 ) {
                    explosionList.remove(i);
                    ex.ex_cnt = 0;
                }
            }else{
                if ( ex.ex_cnt < 7  ) {
                    buffg.drawImage(Explo_img[0], ex.x + 120,
                            ex.y + 15, this);
                }else if ( ex.ex_cnt < 14 ) {
                    buffg.drawImage(Explo_img[1], ex.x + 60,
                            ex.y + 5, this);
                }else if ( ex.ex_cnt < 21 ) {
                    buffg.drawImage(Explo_img[0], ex.x + 5,
                            ex.y + 10, this);
                }else if( ex.ex_cnt > 21 ) {
                    explosionList.remove(i);
                    ex.ex_cnt = 0;
                }
            }
        }
    }


    public void Draw_Missile() {

        for (int i = 0; i < bulletList.size(); i++) {

            bu = (Bullet)bulletList.get(i);
            buffg.drawImage(bullet, bu.x, bu.y, this);
//
        }
    }

    public void Draw_Background(){ //배경 이미지 그리는 부분

 //       buffg.clearRect(0, 0, width, height); //화면 지우기 명령은 이제 여기서 실행합니다.

        buffg.drawImage(backGround1, bx, 0, this);
        buffg.drawImage(backGround1, bx2, 0, this);

        for(int i = 0; i < cx.length; i++) {
            if(cx[i] < 1400) {
                cx[i] += 5 + i * 3;
            }else {cx[i] = 0;}
            buffg.drawImage(Cloud_img[i], 1200 - cx[i], 50+i*200, this);
        }
    }

    /*public void Draw_Char() {    // 실제로 그림들을 그릴 부분

        buffg.drawImage(player, x, y, this);
    }*/

    public void Draw_Player() {
        switch(playerStatus) {
            case 0 :    // 평상시
                /*if((cnt / 5 %2)==0) {
                    buffg.drawImage(player[1], x, y, this);
                }
                else {
                    buffg.drawImage(player[2], x, y, this);
                }*/
                buffg.drawImage(player[0], x, y, this);
                break;
            case 1 : // 충돌
                if((hpCnt/5%2)==0) {
                    buffg.drawImage(player[0], x, y, this);
                }
                else {
                    buffg.drawImage(player[1], x, y, this);
                }
                // buffg.drawImage(player[1], x, y, this);

                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP :
            case KeyEvent.VK_W: KeyUp = true; break;
            case KeyEvent.VK_DOWN :
            case KeyEvent.VK_S : KeyDown = true; break;
            case KeyEvent.VK_LEFT :
            case KeyEvent.VK_A : KeyLeft = true; break;
            case KeyEvent.VK_RIGHT :
            case KeyEvent.VK_D : KeyRight = true; break;
            case KeyEvent.VK_SPACE: KeySpace = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP :
            case KeyEvent.VK_W: KeyUp = false; break;
            case KeyEvent.VK_DOWN :
            case KeyEvent.VK_S : KeyDown = false; break;
            case KeyEvent.VK_LEFT :
            case KeyEvent.VK_A : KeyLeft = false; break;
            case KeyEvent.VK_RIGHT :
            case KeyEvent.VK_D : KeyRight = false; break;
            case KeyEvent.VK_SPACE: KeySpace = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void KeyProcess(){
        if(KeyUp == true && y>-30) {
            y -= 7;
            //playerStatus = 0;
        }
        if(KeyDown == true && y<height-200) {
            y += 7;
            //playerStatus = 0;
        }
        if(KeyLeft == true && x>-68) {
            x -= 7;
            //playerStatus = 0;
        }
        if(KeyRight == true && x<width-210) {
            x += 7;
            //playerStatus = 0;
        }
    }
}