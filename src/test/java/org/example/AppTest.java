package org.example;
import org.example.main.myWin;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    public static void main( String[] args ) throws IOException {


        new myWin().CreatWindow();
        //new ListFrame().List();

    } */
    /*// 解决日志报错问题
        BasicConfigurator.configure();

        // 关闭日志
        // Logger.getRootLogger().shutdown();

        // 加载dll
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "vlc");*/

/*
   @Test
   public static void main( String[] args ){

        VideoMapper videoMapper = MybatisTool.getMapper(VideoMapper.class);
        MarkMapper markMapper=MybatisTool.getMapper(MarkMapper.class);
        List<Video> videos=videoMapper.findAll();
        String strName=videoMapper.findVideoName();
        Integer videoId=videoMapper.findVideoIdByName(strName);

        System.out.println(videos.toString());
       System.out.println(strName);
       System.out.println(videoId);

    }
*/

    @Test
    public static void main( String[] args ){
        new myWin().CreatWindow();
    }


/**
 * 获取主窗口焦点监听器，设置默认焦点为播放暂停按钮
 */
    /*private WindowFocusListener getWindowFocusListener() {
        return new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                setPauseButtonAsDefaultFocus();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                setPauseButtonAsDefaultFocus();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {}
        };
    }*/

/**
 * 视频继续播放定时器
 */
     /*private Timer getContinueTimer() {
        return new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final VideoMapper videoMapper= MybatisTool.getMapper(VideoMapper.class);
                int all=videoMapper.findAllCount();
                long total = getMediaPlayer().getLength();
                long curr = getMediaPlayer().getTime();
                if (curr == total) {
                    int order= videoAll.getVideo_order()+1;
                    videoAll.setVideo_order(order);
                    if (order >= all) {
                        JOptionPane.showMessageDialog(null, "一切都结束了");
                    }
                    String willPlay=videoMapper.findVideoNameByOrder(order);
                    videoAll.setVideo_name(willPlay);
                    play(willPlay);
                    setProgress(getMediaPlayer().getTime(), getMediaPlayer().getLength());
                    progressTimer.restart();
                }
            }
        });
     }*/

    /*fullButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mediaPlayerComponent.getMediaPlayer().setFullScreenStrategy(new DefaultAdaptiveRuntimeFullScreenStrategy(frame));
                //frame.getJMenuBar().setVisible(false);
                //menuBar.setVisible(false);
                buttonPanel.setVisible(false);
                progressPanel.setVisible(false);
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
        });*/

        /*//全屏按钮
        final Button fullButton=new Button("full");
        fullButton.setFocusable(false);
        fullButton.setSize(20,30);
        buttonPanel.add(fullButton);*/
// 设置窗口焦点监听事件：窗口打开时、窗口获得焦点时设置默认焦点为暂停按钮
//frame.addWindowFocusListener(getWindowFocusListener());

// 视频表面焦点监听：表面获得焦点时设置默认焦点为暂停按钮
//mediaPlayerComponent.getVideoSurface().addFocusListener(videoSurfaceFocusAction());

//菜单栏
        /*//全屏按钮
        final Button fullButton=new Button("full");
        fullButton.setFocusable(false);
        fullButton.setSize(20,30);
        buttonPanel.add(fullButton);*/
/**
 * 设置默认焦点为播放暂停按钮
 */
    /*private void setPauseButtonAsDefaultFocus() {
            pauseButton.requestFocus();
    }
*/


}
