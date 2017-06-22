package com.nenu.android.le;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Display extends JFrame
{
    /**
     * @Description:这个类用于界面显示
     * @author
     * @date 2016/06/20
     */
    private static final long serialVersionUID = 1L;

    public static final int BOARD_WIDTH = 700;

    public static final int BOARD_HEIGHT = 600;

    public static final int X_LOCATION = 100;

    public static final int Y_LOCATION = 50;

    private LeProcess leProcess;

    private Display disPlay = this;

    //把界面分为上中三个Panel
    JPanel editPanel = new JPanel();

    JPanel expPanel = new JPanel();

    JPanel denvPanel = new JPanel();

    JPanel centerPanel = new JPanel();

    JPanel buttonPanel = new JPanel();

    //输入表达式
    JTextField expField = new JTextField(30);

    JLabel labelExp = new JLabel("表达式区: ", JLabel.RIGHT);

    //输入动态环境
    JTextField denvField = new JTextField(30);

    JLabel labelDEnv = new JLabel("动态环境: ", JLabel.RIGHT);

    //输出
    JTextArea txtArea = new JTextArea(10, 50);

    JLabel labelTxt = new JLabel("结果:");

    JScrollPane scroll  = new JScrollPane(txtArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


    //按钮
    JButton startButton = new JButton("开始");

    JButton cancelButton = new JButton("取消");

    public void launchFrame()
            throws Exception
    {



        expPanel.add(labelExp);
        expField.setText("ge(add(var(x),mul(cons(2),var(y))),var(z))");
        expPanel.add(expField);

        denvPanel.add(labelDEnv);
        denvField.setText("[x->34, y->7, z->50]");
        denvPanel.add(denvField);

        editPanel.setLayout(new GridLayout(2,1));
        editPanel.add(expPanel);
        editPanel.add(denvPanel);

        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(labelTxt);
        centerPanel.add(scroll);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);

        //界面初始化
        this.setLocation(X_LOCATION, Y_LOCATION);
        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        this.setResizable(true);
        this.setVisible(true);
        this.setTitle("语言Le的计算过程");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1)); //总体为三行一列布局
        this.add(editPanel);
        this.add(centerPanel);
        this.add(buttonPanel);



        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String exp = expField.getText();
                String denv = denvField.getText();
                leProcess=new LeProcess(exp,denv,disPlay);
                leProcess.Start();

            }
        });
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                txtArea.setText("");
            }
        });
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

    public void OutputResult(String result)
    {
        txtArea.append(result + "\n");
    }

    public static void main(String[] args)
            throws Exception
    {
        new Display().launchFrame();
    }

}

