package com.ericcumbee.dlid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {


    public static void main(String[] args) {
        TestUI();


    }

    public static void TestUI(){

        final JTextField txt = new JTextField();
        txt.setVisible(true);
        JFrame j = new JFrame();
        JPanel jp = new JPanel();
        JButton btn = new JButton();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Test = txt.getText();
                parser p = new parser();
                DLicense d = p.parse(Test);
                System.out.print(d);

                System.exit(0);
            }
        });
        btn.setText("Test");
        Container container = j.getContentPane();
        container.add(txt);
        container.add(btn,BorderLayout.SOUTH);
        j.pack();
        j.setVisible(true);
    }
}
