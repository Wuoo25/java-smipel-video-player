package org.example.main;


import org.example.AllClass.Mark;
import org.example.AllClass.Video;
import org.example.Mapper.MarkMapper;
import org.example.Mapper.VideoMapper;
import org.example.Tool.MybatisTool;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class myWin {

    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final JFrame frame=new JFrame("My First Media Player");
    private  JProgressBar progressBar;
    private  JButton pauseButton;
    private  JButton rewindButton;
    private  JButton skipButton;
    //重播
    private JButton rePlay;
    //添加书签按钮
    private JButton addButton;
    // 打开文件对话框
    private FileDialog fileDialog;
    // 播放文件列表窗口
    private JFrame listWindow;
    // 播放文件列表按钮
    private Button listButton;
    //音量键
    private JSlider volumnContr;
    // 总时间
    private static String TOTAL_TIME;
    // 进度定时器
    private Timer progressTimer;
    // 进度条面板
    private JPanel progressPanel;
    //底部面板
    private JPanel buttonPanel;
    // 显示时间
    private Label displayTime;
    private static final int PROGRESS_HEIGHT = 20;
    private static final int PROGRESS_MIN_VALUE = 0;
    private static final int PROGRESS_MAX_VALUE = 100;
    private static final int WINDOW_X = 100;
    private static final int WINDOW_Y = 100;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final int LIST_WINDOW_WIDTH = 400;
    // 播放速度
    private float speed;
    //播放面板
    private JPanel player;
    // 显示播放速度的按钮
    private Label displaySpeed;
    //书签窗口
    private JFrame markFrame;
    private JPanel markPane;
    private JTextArea markText;
    private JButton yes;
    private JButton no;
    //sql变量
    private static Video videoAll;

    //视频列表相关变量
    //列表显示总Panel
    private JPanel listContent;
    //列表创建JList
    private JList<Video> videoList;
    DefaultListModel<Video> model= new DefaultListModel<>();
    private JButton delete;
    private JButton playLast;
    private JButton playThis;
    private JButton playNext;

    /**
     * 视频列表窗口
     */
    public JPanel ListCreate() {

        final VideoMapper videoMapper= MybatisTool.getMapper(VideoMapper.class);
        //读取全部视频信息
        java.util.List<Video> videoLiseInfo=videoMapper.findAll();
        model.removeAllElements();
        //循环显示加入list
        for (Video videoSingle:videoLiseInfo){
            model.addElement(videoSingle);
            System.out.println(videoSingle);
        }
        videoList=new JList<>(model);
        videoList.updateUI();
        //设置JList
        videoList.setSelectionBackground(Color.gray);
        videoList.setVisibleRowCount(20);
        //单选
        videoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //视频列表
        listContent=new JPanel();
        listContent.add(new JScrollPane(videoList));
        //底部按钮栏
        JPanel bottomP=new JPanel();
        //随机播放
        JButton randPlay=new JButton("随机播放");
        randPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Random rand=new Random();
                int all=videoMapper.findAllCount();
                int randOrder= rand.nextInt(all)+1;
                videoAll.setVideo_order(randOrder);
                String willPlay=videoMapper.findVideoNameByOrder(randOrder);
                videoAll.setVideo_name(willPlay);
                play(willPlay);
            }
        });
        bottomP.add(randPlay,BorderLayout.CENTER);
        //删除按钮
        delete=new JButton("删除");
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedId= videoList.getSelectedIndex();
                if(selectedId>=0){
                    model.remove(selectedId);
                }
            }
        });
        bottomP.add(delete,BorderLayout.CENTER);
        //播放上一个按钮
        playLast=new JButton("上一曲");
        playLast.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int VideoOrder=videoList.getSelectedIndex();
                videoAll.setVideo_order(VideoOrder);
                if(VideoOrder>0){
                    String willPlay=videoMapper.findVideoNameByOrder(VideoOrder);
                    videoAll.setVideo_name(willPlay);
                    play(willPlay);
                }else {
                    JOptionPane.showMessageDialog(null, "前面一个也没有了");
                }

            }
        });
        bottomP.add(playLast,BorderLayout.CENTER);
        //播放当前按钮
        playThis=new JButton("播放");
        playThis.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int VideoOrder=videoList.getSelectedIndex()+1;
                String willPlay=videoMapper.findVideoNameByOrder(VideoOrder);
                int all=videoMapper.findAllCount();
                videoAll.setVideo_order(VideoOrder);
                videoAll.setVideo_name(willPlay);
                if(VideoOrder>0&&VideoOrder<=all){
                    play(willPlay);
                }else {
                    JOptionPane.showMessageDialog(null, "你可能要重新选一下");
                }
            }
        });
        bottomP.add(playThis,BorderLayout.CENTER);
        //播放下一个按钮
        playNext=new JButton("下一曲");
        playNext.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int VideoOrder=videoList.getSelectedIndex()+2;
                videoAll.setVideo_order(VideoOrder);
                int all=videoMapper.findAllCount();
                if(VideoOrder<=all){
                    String willPlay=videoMapper.findVideoNameByOrder(VideoOrder);
                    videoAll.setVideo_name(willPlay);
                    play(willPlay);
                }else {
                    JOptionPane.showMessageDialog(null, "后面一个也没有了");
                }

            }
        });
        bottomP.add(playNext,BorderLayout.CENTER);
        listContent.add(bottomP,BorderLayout.SOUTH);

        listContent.updateUI();
        return listContent;

    }

    /**
     * 书签窗口
     */
    public void markCreate(){
        //窗口
        markFrame=new JFrame("这里是书签");
        //总面板
        markPane=new JPanel();
        //文本域
        markText=new JTextArea("请输入内容：",7,30);
        markText.setLineWrap(true);
        markText.setFont(new Font("楷体",Font.BOLD,16));    //修改字体样式
        markText.setBackground(Color.LIGHT_GRAY);    //设置按钮背景色
        JScrollPane jsp=new JScrollPane(markText);    //将文本域放入滚动窗口
        Dimension size=markText.getPreferredSize();    //获得文本域的首选大小
        jsp.setBounds(110,90,size.width,size.height);

        yes=new JButton("确定");
        yes.addMouseListener(GetMarkContent());

        no=new JButton("取消");
        no.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                markFrame.setDefaultCloseOperation(0);
            }
        });

        markPane.add(jsp,BorderLayout.CENTER);
        markPane.add(yes,BorderLayout.SOUTH);
        markPane.add(no,BorderLayout.SOUTH);

        //添加面板到窗口
        markFrame.add(markPane);
        //窗口设置
        markFrame.setBounds(300,200,600,200);
        markFrame.setVisible(true);
        markFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }

    /**
     * 主窗口
     */
    public void CreatWindow() {

        //实例化
        videoAll=new Video();

        // 设置默认速度为原速
        speed = 1.0f;
        //主窗口frame
        frame.setBounds(200, 200, 1400, 800);
        frame.setTitle("VideoPlayer-"+getTitleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 窗口关闭事件：释放资源并退出程序
        frame.addWindowListener(closeWindowReleaseMedia());

        //主面板
        final JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));

        //播放面板
        player = new JPanel();
        contentPane.add(player, BorderLayout.CENTER);
        player.setLayout(new BorderLayout(0, 0));
        //创建播放器组件并添加到容器中去
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        player.add(mediaPlayerComponent);

        final JMenuBar menuBar=new JMenuBar();
        //帮助菜单项
        JMenu help=new JMenu("帮助");
        menuBar.add(help);
        help.addMenuListener(OpenHelp());
        //书签菜单项
        final JMenu markMenu=new JMenu("书签");
        markMenu.setSize(20,20);
        menuBar.add(markMenu);
        markMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                markMenu.removeAll();
                MarkMapper markMapper = MybatisTool.getMapper(MarkMapper.class);
                VideoMapper videoMapper = MybatisTool.getMapper(VideoMapper.class);
                int NowId=videoMapper.findVideoIdByName(videoAll.getVideo_name());

                java.util.List<Mark> markList = markMapper.getAllByVideoId(NowId);
                if(markList!=null){
                    for (Mark mark : markList) {
                        String Time=formatSecond2Time(mark.getMark_time());
                        JMenuItem markItem = new JMenuItem("{"+mark.getMark_text() + "}时间是：{" + Time + "}");
                        final long time=mark.getMark_time();
                        markItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //播放时间
                                getMediaPlayer().setTime(time);
                                //进度条
                                long total = mediaPlayerComponent.getMediaPlayer().getLength();
                                setProgress(time,total);
                            }
                        });
                        markMenu.add(markItem);
                    }
                }else{
                    //弹出消息框
                    JOptionPane.showMessageDialog(null, "需要先选择一下奥");
                }

                player.updateUI();
            }
            @Override
            public void menuDeselected(MenuEvent e) {

            }
            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        player.add(menuBar,BorderLayout.SOUTH);

        // 底部面板
        JPanel bottomPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(bottomPanel, BoxLayout.Y_AXIS);
        bottomPanel.setLayout(boxLayout);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        //进度条面板
        progressPanel = new JPanel();
        progressBar=new JProgressBar();
        progressPanel.setSize(getNewDimension());
        progressBar.setMinimum(PROGRESS_MIN_VALUE);
        progressBar.setMaximum(PROGRESS_MAX_VALUE);

        //进度条显示视频进度
        //设置进度条中间显示进度百分比
        progressBar.setStringPainted(true);
        // 进度条进度的颜色
        progressBar.setForeground(new Color(46, 145, 228));
        // 进度条背景的颜色
        progressBar.setBackground(new Color(220, 220, 220));

        // 点击进度条调整视频播放指针
        progressBar.addMouseListener(setVideoPlayPoint());

        // 定时器
        progressTimer = getProgressTimer();

        progressPanel.add(progressBar);
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(progressPanel,BorderLayout.NORTH);
        //contentPane.add(progressPanel, BorderLayout.SOUTH);

        //按钮组件面板
        buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(buttonPanel,BorderLayout.CENTER);

        //截屏按钮
        JButton ScreenButton=new JButton("截屏");
        buttonPanel.add(ScreenButton);
        ScreenButton.addMouseListener(UploadImage());

        //重播按钮
        rePlay=new JButton("重播");
        rePlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rePlay(videoAll.getVideo_name());
            }
        });
        buttonPanel.add(rePlay);

        // 选择文件按钮
        Button chooseButton = new Button("choose");
        fileDialog = new FileDialog(this.frame);
        fileDialog.setMultipleMode(true);
        chooseButton.setFocusable(false);
        chooseButton.addMouseListener(mouseClickedChooseFiles());
        buttonPanel.add(chooseButton);

        //添加书签按钮
        addButton=new JButton("add Mark");
        buttonPanel.add(addButton);
        addButton.addMouseListener(AddMark());
        //markMapper.InsertMark(markWin.HaveTextContent());

        //滑块显示音量
        volumnContr=new JSlider();
        volumnContr.setPaintLabels(true);
        volumnContr.setSnapToTicks(true);
        volumnContr.setValue(100);

        volumnContr.setMinimum(0);
        volumnContr.setMaximum(120);
        buttonPanel.add(volumnContr,BorderLayout.WEST);
        //因为音量最大值和JSlider控件本身长度在数值上市不相等的，需要换算.
        //即鼠标的横坐标（相对JSlider起始点横坐标而言）占JSlider的比例乘上最大音量。
        volumnContr.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volumnContr.setValue((int)e.getX()*
                        (volumnContr.getMaximum()/volumnContr.getWidth()));
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //控制音量
        volumnContr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                mediaPlayerComponent.getMediaPlayer().setVolume(volumnContr.getValue());
            }
        });

        //播放时间
        displayTime = new Label();
        displayTime.setText(getTimeString());
        buttonPanel.add(displayTime);

        //回退按钮
        rewindButton = new JButton("Rewind");
        buttonPanel.add(rewindButton,BorderLayout.SOUTH);
        //暂停按钮
        pauseButton = new JButton("Pause");
        pauseButton.setBackground(new Color(115, 173, 194));
        buttonPanel.add(pauseButton,BorderLayout.SOUTH);
        //前进按钮
        skipButton = new JButton("Skip");
        buttonPanel.add(skipButton,BorderLayout.SOUTH);
        //倍速按钮：每次递增0.5，最大为3倍速
        Button fastForwardButton = new Button(">>>");
        fastForwardButton.setFocusable(false);
        buttonPanel.add(fastForwardButton,BorderLayout.SOUTH);

        //播放速度显示标签
        displaySpeed = new Label();
        displaySpeed.setText("x" + speed);
        displaySpeed.setFocusable(false);
        displaySpeed.setEnabled(false);
        buttonPanel.add(displaySpeed);

        // 播放文件列表按钮
        listButton = new Button("list");
        listButton.setFocusable(false);
        listButton.addMouseListener(mouseClickedSetListWindow());
        buttonPanel.add(listButton);

        //回退
        rewindButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaPlayer().skip(-1000);
            }
        });
        //暂停
        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //暂停
                mediaPlayerComponent.getMediaPlayer().pause();
                if (progressTimer.isRunning()) {
                    progressTimer.stop();
                } else {
                    progressTimer.restart();
                }
                pauseButton.setBackground(new Color(255, 198, 0));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pauseButton.setBackground(new Color(141, 193, 201));
            }
        });
        pauseButton.addKeyListener(spaceKeyPressMediaPause());
        //前进
        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaPlayer().skip(1000);
            }
        });
        //倍速
        fastForwardButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (speed >= 3.0f) {
                    speed = 1.0f;
                } else {
                    speed += 0.5f;
                }
                getMediaPlayer().setRate(speed);
                displaySpeed.setText("x" + speed);
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        // 监听窗口大小，设置进度条宽度为窗口宽度
        frame.addComponentListener(windowResizedResetProgressWidth());
        // 监听窗口最大化和还原，设置进度条宽度为窗口宽度
        frame.addWindowStateListener(windowStateChangedResetProgressWidth());

        // 设置窗口最小值
        frame.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        frame.setContentPane(contentPane);
        frame.setVisible(true);

    }


    /**
     * 获取视频名称
     * @return
     */
    private String getTitleName() {
        if(videoAll.getVideo_name()!=null){
            String title= videoAll.getVideo_name();
            return title;
        }else
            return "嘤这里什么也没有";

    }

    /**
     * 打开文件
     * @return
     */
    private MouseListener mouseClickedChooseFiles() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser=new JFileChooser();
                int mount=chooser.showOpenDialog(null);
                if(mount==JFileChooser.APPROVE_OPTION){
                    File fileChoose=chooser.getSelectedFile();
                    VideoMapper videoMapper=MybatisTool.getMapper(VideoMapper.class);
                    Video video=videoMapper.findAllByName(fileChoose.getAbsolutePath());
                    Integer count=videoMapper.findAllCount();
                    if(video==null){
                        video=new Video();
                        video.setVideo_name(fileChoose.getAbsolutePath());
                        video.setVideo_order(count+1);
                        //插入数据库
                        videoMapper.InsertVideo(video);
                        video=videoMapper.findAllByName(fileChoose.getAbsolutePath());
                        System.out.println(video);
                    }
                    //全局变量videoAll类被赋值
                    videoAll=video;
                    play(fileChoose.getAbsolutePath());
/*
                    setProgress(0, 0);
                    //progressTimer.stop();
*/
                    ListCreate();
                }
            }
        };
    }

    /**
     * 下载图片
     * @return
     */
    public MouseListener UploadImage() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage UploadImage=mediaPlayerComponent.getMediaPlayer().getSnapshot();
                //时间戳生成新名字
                String newName = UUID.randomUUID().toString() + System.currentTimeMillis();
                File outputFileName  = new File("D:\\Pictures\\"+newName+".png");
                System.out.println(outputFileName);
                try {
                    ImageIO.write(UploadImage,  "PNG",  outputFileName);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        };
    }

    /**
     * 得到书签内容
     * @return
     */
    public MouseListener GetMarkContent() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //String time=formatSecond2Time(mediaPlayerComponent.getMediaPlayer().getTime());
                Long time1=mediaPlayerComponent.getMediaPlayer().getTime();
                String str=markText.getText();
                MarkMapper markMapper= MybatisTool.getMapper(MarkMapper.class);
                Mark mark=new Mark();
                mark.setMyVideoId(videoAll.getVideo_order()+1);
                mark.setMark_text(str);
                mark.setMark_time(time1);
                //插入语句
                markMapper.InsertMark(mark);
                System.out.println(mark);
            }
        };
    }

    private MouseListener AddMark() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               markCreate();
            }
        };
    }

    /**
     * 鼠标点击设置播放列表窗口
     */
    private MouseListener mouseClickedSetListWindow() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listWindow == null) {
                    // 播放文件列表窗口
                    listWindow = new JFrame();
                    listWindow.add(ListCreate());;
                    listWindow.setUndecorated(true);
                    // 设置透明度
                    listWindow.setOpacity(0.8f);
                    setListWindowBounds();
                    listWindow.setVisible(true);
                    setListButtonColorWhenListWindowShown();
                    listWindow.addComponentListener(
                            setListButtonBackgroundWhenListWindowShownOrHidden());
                    return;
                }
                int x = frame.getX();
                int width = frame.getWidth();
                if (WINDOW_X != x || WINDOW_WIDTH != width) {
                    setListWindowBounds();
                }
                boolean visible = listWindow.isVisible();
                if (visible) {
                    listWindow.setVisible(false);
                } else {
                    listWindow.setVisible(true);
                }
            }
        };
    }

    /**
     * 当播放列表隐藏或显示时，设置播放列表按钮的背景颜色
     */
    private ComponentListener setListButtonBackgroundWhenListWindowShownOrHidden() {
        return new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                setListButtonColorWhenListWindowShown();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                setListButtonColorWhenListWindowHidden();
            }
        };
    }

    /**
     * 当播放列表隐藏时，设置播放列表按钮背景颜色
     */
    private void setListButtonColorWhenListWindowHidden() {
        listButton.setBackground(new Color(238, 238, 238));
    }

    /**
     * 当播放列表显示时，设置播放列表按钮背景颜色
     */
    private void setListButtonColorWhenListWindowShown() {
        listButton.setBackground(new Color(100, 133, 175));
    }

    /**
     * 设置播放列表边界
     */
    private void setListWindowBounds() {

            if (listWindow != null) {
                listWindow.setBounds(frame.getWidth() + frame.getX() - LIST_WINDOW_WIDTH - 6, frame.getY() + 37,
                        LIST_WINDOW_WIDTH - 8, frame.getHeight() - 100);
            }
    }

    /**
     * 重新播放
     * @param video_name
     */
    private void rePlay(String video_name) {
        String str=video_name;
        mediaPlayerComponent.getMediaPlayer().playMedia(str);
        mediaPlayerComponent.getMediaPlayer().setTime(0);
        //获取时间
        long time=mediaPlayerComponent.getMediaPlayer().getMediaMeta().getLength();
        progressTimer.restart();
        //设置进度条
        setProgress(0,time);
        //获取进度条
        getProgress();
        player.updateUI();
        progressPanel.updateUI();
        buttonPanel.updateUI();
    }

    /**
     * 播放
     */
    public void play(String str) {
        mediaPlayerComponent.getMediaPlayer().playMedia(str);
        long time=mediaPlayerComponent.getMediaPlayer().getMediaMeta().getLength();
        progressTimer.restart();
        setProgress(0,time);
        getProgress();
        player.updateUI();
        progressPanel.updateUI();
        buttonPanel.updateUI();
    }

    /**
     * 关闭程序
     * @return
     */
    private WindowListener closeWindowReleaseMedia() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getMediaPlayer().stop();
                getMediaPlayer().release();
                System.exit(0);
            }
        };
    }

    /**
     * 改变进度条大小适应窗口
     * @return
     */
    private WindowAdapter windowStateChangedResetProgressWidth() {
        return new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent state) {
                // state=1或7为最小化，此处不处理

                if (state.getNewState() == 0) {
                    // System.out.println("窗口恢复到初始状态");
                    setProgressWidthAutoAdaptWindow();
                } else if (state.getNewState() == 6) {
                    // System.out.println("窗口最大化");
                    setProgressWidthAutoAdaptWindow();
                }
            }
        };

    }

    private void setProgressWidthAutoAdaptWindow() {
        getProgress().setPreferredSize(getNewDimension());
    }

    /**
     *主窗口大小变化监听器，当主窗口大小改变时，重绘进度条，并隐藏播放视频列表窗口
     * @return
     */
    private ComponentAdapter windowResizedResetProgressWidth() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setProgressWidthAutoAdaptWindow();
            }
        };
    }

    /**
     * 定时器相关函数
     */
    private Timer getProgressTimer() {
        return new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getProgress().getValue() >= PROGRESS_MAX_VALUE) {
                    // 结束定时器
                    progressTimer.stop();
                    return;
                }
                // 设置进度值
                setProgress(getMediaPlayer().getTime(), getMediaPlayer().getLength());
            }
        });
    }

    /**
     *进度条显示
     */
    private Dimension getNewDimension() {
        return new Dimension(mediaPlayerComponent.getWidth(), PROGRESS_HEIGHT);
    }

    /**
     *进度条跳转动作
     */
    private MouseAdapter setVideoPlayPoint() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                long total = mediaPlayerComponent.getMediaPlayer().getLength();
                long time = (long) ((float) x / progressBar.getWidth() * total);
                //跳转进度条设置
                setProgress(time, total);
                //跳转时间设置
                mediaPlayerComponent.getMediaPlayer().setTime(time);
            }
        };
    }

    /**
     *设置进度条
     */
    private void setProgress(long curr, long total) {
        float percent = (float) curr / total;
        int value = (int) (percent * 100);
        getProgress().setValue(value);
        //播放时间
        displayTime.setText(getTimeString(curr, total));
    }

    private JProgressBar getProgress() {
        return progressBar;
    }

    /**
     * 获取播放时间显示字符串
     * @return 播放时间显示字符串，格式00:00:00/00:00:00
     */
    private String getTimeString(long curr, long total) {
        return formatSecond2Time(curr) + " / " + formatSecond2Time(total);
    }

    /**
     * 格式化时间，将秒转化为时分秒
     * @param milliseconds 毫秒时间
     * @return 00:00:00
     */
    private String formatSecond2Time(long milliseconds) {
        int second = (int) (milliseconds / 1000);
        int h = second / 3600;
        int m = (second % 3600) / 60;
        int s = (second % 3600) % 60;
        return String.format("%02d", h) + ":" + String.format("%02d", m) + ":"
                + String.format("%02d", s);
    }
    public String getTimeString() {
        setTotalTime();
        return formatSecond2Time(getMediaPlayer().getTime()) + " / " + TOTAL_TIME;
    }
    /**
     * 设置播放时间显示字符串中的总时间
     */
    private void setTotalTime() {
        if (TOTAL_TIME == null) {
            long totalSecond = getMediaPlayer().getLength();
            TOTAL_TIME = formatSecond2Time(totalSecond);
        }
    }

    /**
     *暂停空格键
     */
    private KeyListener spaceKeyPressMediaPause() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    getMediaPlayer().pause();
                }
            }
        };
    }

    /**
     * 方便使用
     * @return
     */
    private EmbeddedMediaPlayer getMediaPlayer() {
        return mediaPlayerComponent.getMediaPlayer();
    }

    /**
     * 打开帮助文档
     * @return
     */
    private MenuListener OpenHelp() {
        return new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                JOptionPane.showMessageDialog(null, "本说明书给予使用本视频播放软件的用户，便于用户更加顺利和方便的使用本视频播放器。\n" +
                        "1.功能 \n" +
                        "（1）具备播放、暂停、顺序切换、全屏等基本功能的视频播放软件功能，能够支持常见视频文件格式MP4等；\n" +
                        "（2）可以自定义播放列表，并在播放列表中自由组织需要播放的视频，能够自由添加和删除视频；\n" +
                        "（3）可以为播放的视频中添加“书签”，“书签”可使用户将视频快速定位到对应的时间点播放；“书签”可以用文字撰写添加书签的原因并展示；\n" +
                        "（4）可以将视频当前播放的一帧画面保存到硬盘，即对画面进行截屏。\n" +
                        "（5）视频播放可对视频进行倍速播放；\n" +
                        "（6）视频播放可以在列表中随机播放。\n" +
                        "2.补充说明\n" +
                        "该播放器可以进行简单的播放界面操作、播放列表操作、书签操作和截屏操作，说明如上。\n" +
                        "如有问题，请及时反馈。");
            }
            @Override
            public void menuDeselected(MenuEvent e) {

            }
            @Override
            public void menuCanceled(MenuEvent e) {

            }
        };

    }

}
