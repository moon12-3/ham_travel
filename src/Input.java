import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Input extends JFrame {
    int width, height;
    Toolkit tk = Toolkit.getDefaultToolkit();

    Font font;
    Frame_make frame_make;
    String name;
    int score;
    JLabel labelGetScore;
    Clip ost;

    Image backGround = new ImageIcon("src/img/back_input.png").getImage();
    ImageIcon iconSave = new ImageIcon("src/img/btn_to_save.png");

    public void init() throws IOException, FontFormatException { // 컴포넌트 세팅
        width = 1200;
        height = 800;

    }

    public void setScore(int score) {
        this.score = score;
    }

    public Input() throws IOException, FontFormatException {
        super("input"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();

        ost = Sound("src/bgm/intro.wav", true);

        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);

        this.setSize(width, height);
        String newScor = Integer.toString(score);

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

        JLabel labelName = new JLabel("NAME");
        labelName.setFont(font.deriveFont(Font.BOLD, 43));
        labelName.setHorizontalAlignment(JLabel.CENTER);
        labelName.setBounds(300, 295, 300, 70);
        
        JTextField textName = new JTextField();
        textName.setBounds(560, 305, 230, 50);
        textName.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        textName.setFont(font.deriveFont(Font.BOLD, 30));
        name = textName.getText();

        JLabel labelScore = new JLabel("SCORE");
        labelScore.setFont(font.deriveFont(Font.BOLD, 43));
        labelScore.setHorizontalAlignment(JLabel.CENTER);
        labelScore.setBounds(300, 415, 300, 70);

        // 게임 점수 전달
        labelGetScore = new JLabel("");
        labelGetScore.setBounds(560, 425, 230, 50);
        labelGetScore.setFont(font.deriveFont(Font.BOLD, 30));

        JButton btnSave = new JButton(iconSave);
        btnSave.setBounds(485, 530, 230, 80);
        btnSave.setBorderPainted(false);
        btnSave.setContentAreaFilled(false);
        btnSave.setFocusPainted(false);

        JLabel labelSave = new JLabel("SAVE");
        labelSave.setFont(font.deriveFont(Font.BOLD, 43));
        labelSave.setHorizontalAlignment(JLabel.CENTER);
        labelSave.setBounds(485, 530, 230, 80);

        panel.add(labelName);
        panel.add(textName);
        panel.add(labelScore);
        panel.add(labelGetScore);
        panel.add(labelSave);
        panel.add(btnSave);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DBcon db = new DBcon();

                // db insert
                PreparedStatement ps= null;
                try {
                    ps = db.getCon().prepareStatement("insert into ham_score(userName, userScore) " +
                            "values('"+textName.getText()+"', "+score+");");
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {
                    new Rank();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (FontFormatException ex) {
                    ex.printStackTrace();
                }
                ost.stop();
                setVisible(false); // 창 안보이게 하기
            }
        });


/*
        JButton btnIntro = new JButton(introIcon);
        btnIntro.setBounds(40, 655, 90, 90);
        btnIntro.setBorderPainted(false);
        btnIntro.setContentAreaFilled(false);
        btnIntro.setFocusPainted(false);

        JLabel labelName = new JLabel("NAME");
        labelName.setFont(new Font(null, Font.BOLD, 42));
        labelName.setHorizontalAlignment(JLabel.CENTER);
        JLabel scoreLabel = new JLabel("SCORE");
        scoreLabel.setFont(new Font(null, Font.BOLD, 42));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);

        labelName.setBounds(315, 170, 150, 65);
        scoreLabel.setBounds(735, 170, 150, 65);

        panel.add(labelName);
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
        ResultSet resultSet = st.executeQuery("SELECT * FROM ( SELECT * FROM ham_score ORDER BY userScore DESC )A LIMIT 5");
        int i=0;
        while(resultSet.next()){
            String name = resultSet.getString("userName");
            int score = resultSet.getInt("userScore");

            nameArr[i] = new JLabel(name);
            nameArr[i].setFont(new Font(null, Font.BOLD, 40));
            nameArr[i].setBounds(315, 270+70*i, 150, 60);
            nameArr[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(nameArr[i]);

            scoreArr[i] = new JLabel(Integer.toString(score));
            scoreArr[i].setFont(new Font(null, Font.BOLD, 40));
            scoreArr[i].setBounds(735, 270+70*i, 150, 60);
            scoreArr[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(scoreArr[i]);

            System.out.println(name+" "+score);
            i++;
        }

        db.getCon().close();
        st.close();
        resultSet.close();
*/

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

    class DBuse {

    }

    public static void main(String[] args) throws SQLException, IOException, FontFormatException {
        new Input();
    }

    public Clip Sound(String file, boolean Loop){ //사운드 재생용 메소드
        Clip clip = null;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
            clip = AudioSystem.getClip();
            clip.open(ais);

            clip.start();
            if (Loop) clip.loop(-1);   // 계속 재생할 것인지

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }
}

