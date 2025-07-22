package com.uifx;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CollapsiblePanel extends JPanel
{
    private final JPanel contentPanel;
    private final JLabel headerLabel;
    private boolean expanded = false;
    private final String tabTitle;
    private final Color titleColor = new Color(220, 138, 0);
    private final Color hoverColor = new Color(240, 234, 214); // cream
    private final Color arrowColor = new Color(136, 136, 136); // gray

    public CollapsiblePanel(String title, JPanel content)
    {
        this.tabTitle = title.replaceFirst("^> ", "");

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(2, 0, 2, 0));

        contentPanel = content;
        contentPanel.setVisible(false); // start collapsed

        headerLabel = new JLabel();
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
        headerLabel.setBorder(new EmptyBorder(4, 4, 4, 0));
        headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateLabelColor(false);

        headerLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                toggle();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                updateLabelColor(true);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                updateLabelColor(false);
            }
        });

        add(headerLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void toggle()
    {
        expanded = !expanded;
        contentPanel.setVisible(expanded);
        updateLabelColor(false);
        revalidate();
        repaint();
    }

    private void updateLabelColor(boolean hovering)
    {
        String arrow = expanded ? "v" : ">";
        Color activeColor = hovering ? hoverColor : titleColor;
        String html = String.format(
                "<html><font color='#%02x%02x%02x'>%s</font> <font color='#%02x%02x%02x'>%s</font></html>",
                arrowColor.getRed(), arrowColor.getGreen(), arrowColor.getBlue(), arrow,
                activeColor.getRed(), activeColor.getGreen(), activeColor.getBlue(), tabTitle);
        headerLabel.setText(html);
    }
}
