import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Intro extends JFrame {
    int width;
    int height;

    // 사용 이미지 불러오기
    Image backGround = new ImageIcon("src/img/background1.png").getImage();
    ImageIcon startIcon = new ImageIcon("src/img/start_btn.png");
    ImageIcon readyIcon = new ImageIcon("src/img/ready_btn.png");
    ImageIcon rankIcon = new ImageIcon("src/img/rank_btn.png");
    ImageIcon logoIcon = new ImageIcon("src/img/main_logo.png");

    public void init() { // 컴포넌트 세팅
        width = 1200;
        height = 800;
    }

    public Intro() {
        super("intro"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyPanel panel = new MyPanel();
        panel.setLayout(null);

        JButton btnStart = new JButton(startIcon);
        JButton btnRank = new JButton(rankIcon);
        JButton btnReady = new JButton(readyIcon);
        JLabel labelLogo = new JLabel(logoIcon);

        init();

        // 버튼, 라벨 크기 & 위치 설정
        labelLogo.setBounds(395, 200, 400, 300);

        btnStart.setBounds(160, 500, 160, 160);
        btnStart.setBorderPainted(false);
        btnStart.setContentAreaFilled(false);
        btnStart.setFocusPainted(false);

        btnRank.setBounds(520, 500, 160, 160);
        btnRank.setBorderPainted(false);
        btnRank.setContentAreaFilled(false);
        btnRank.setFocusPainted(false);

        btnReady.setBounds(880, 500, 160, 160);
        btnReady.setBorderPainted(false);
        btnReady.setContentAreaFilled(false);
        btnReady.setFocusPainted(false);

        panel.add(labelLogo);
        panel.add(btnStart);
        panel.add(btnRank);
        panel.add(btnReady);

        this.setSize(width, height);
        this.add(panel);
        this.setVisible(true);
        this.setResizable(false);

        // 버튼 기능 설정
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Frame_make();
                setVisible(false); // 창 안보이게 하기
            }
        });

        btnRank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Frame_make();
                setVisible(false); // 창 안보이게 하기
            }
        });

        btnReady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Frame_make();
                setVisible(false); // 창 안보이게 하기
            }
        });
    }

    class MyPanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backGround, 0, 0,width, height, this);

        }
    }
    public static void main(String[] args) {
        new Intro();
    }
}