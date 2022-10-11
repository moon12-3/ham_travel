import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String args[]) {
        new Frame_make();
    }
}

class Frame_make extends JFrame {
    int width = 1200;
    int height = 800;
    Toolkit tk = Toolkit.getDefaultToolkit();   // 이미지 불러오는 툴킷
    Image player = tk.getImage("../img/player.png");

    Frame_make() {
        super("햄모험");
        setSize(width, height);
        init();
        start();
//        paint();
        Dimension screen = tk.getScreenSize();
//        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); // 현재 모니터 해상도 값
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);
        setVisible(true);
        setResizable(false);

    }

    public void init() { // 컴포넌트 세팅
    }

    public void start() { // 시작처리명령
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void paint(@NotNull Graphics g) {

        g.clearRect(0, 0, width, height); // 0,0 에서 위에서 정한 해상도 크기만큼 화면을 지움
        g.drawImage(player, 100, 100, this); // (100, 100)에 player 이미지를 그려넣음

    }
}
