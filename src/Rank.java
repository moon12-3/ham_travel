import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class Rank extends JFrame {
    int width;
    int height;
    JLabel[] nameArr = new JLabel[5];
    JLabel[] scoreArr = new JLabel[5];
    Font font;

    // 사용 이미지 불러오기
    Image backGround = new ImageIcon("src/img/rankbackground.png").getImage();
    ImageIcon introIcon = new ImageIcon("src/img/back_intro.png");

    public void init() throws IOException, FontFormatException { // 컴포넌트 세팅
        width = 1200;
        height = 800;

    }

    public Rank() throws SQLException, IOException, FontFormatException {
        super("rank"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();

        MyPanel panel = new MyPanel();
        panel.setLayout(null);

        // 외부 글꼴 적용
        try {
            InputStream inputStream = new BufferedInputStream(
                    new FileInputStream("src/font/dunggeunmo.ttf"));

            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        JButton btnIntro = new JButton(introIcon);
        btnIntro.setBounds(40, 655, 90, 90);
        btnIntro.setBorderPainted(false);
        btnIntro.setContentAreaFilled(false);
        btnIntro.setFocusPainted(false);

        JLabel nameLabel = new JLabel("NAME");
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setFont(font.deriveFont(Font.BOLD, 43));
        JLabel scoreLabel = new JLabel("SCORE");
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setFont(font.deriveFont(Font.BOLD, 43));

        nameLabel.setBounds(315, 170, 150, 65);
        scoreLabel.setBounds(735, 170, 150, 65);

        panel.add(nameLabel);
        panel.add(scoreLabel);
        panel.add(btnIntro);

        btnIntro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Intro();
                setVisible(false); // 창 안보이게 하기
            }
        });

        DBcon db = new DBcon();

        // db select
        Statement st = db.getCon().createStatement();
        ResultSet resultSet = st.executeQuery("SELECT * FROM " +
                "( SELECT * FROM ham_score ORDER BY userScore DESC )A " +
                "LIMIT 5");
        int i=0;
        while(resultSet.next()){
            String name = resultSet.getString("userName");
            int score = resultSet.getInt("userScore");

            nameArr[i] = new JLabel(name);
            nameArr[i].setFont(font.deriveFont(Font.BOLD, 40));
            nameArr[i].setBounds(315, 270+70*i, 150, 60);
            nameArr[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(nameArr[i]);

            scoreArr[i] = new JLabel(Integer.toString(score));
            scoreArr[i].setFont(font.deriveFont(Font.BOLD, 40));
            scoreArr[i].setBounds(735, 270+70*i, 150, 60);
            scoreArr[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(scoreArr[i]);

            System.out.println(name+" "+score);
            i++;
        }

        db.getCon().close();
        st.close();
        resultSet.close();

        this.setSize(width, height);
        this.add(panel);
        this.setVisible(true);
        this.setResizable(false);
    }

    class MyPanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backGround, 0, 0,width, height, this);

        }
    }

    public static void main(String[] args) throws SQLException, IOException, FontFormatException {
        new Rank();
    }
}
