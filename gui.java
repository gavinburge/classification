
// Java program to create open or
// save dialog using JFileChooser
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

class gui extends JFrame implements ActionListener {
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Jlabel to show the files user selects
    static JLabel l;
    static List<Train> trainDataset = new ArrayList<Train>();
    static byte[] image;
  
    // a default constructor
    gui()
    {
    }

	public static void main(String args[]){

        JFrame f = new JFrame("file chooser");
        f.setSize(400, 400);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // button to open open dialog
        JButton button1 = new JButton("openTrainingData");
        JButton button2 = new JButton("openImage");
        JButton button3 = new JButton("process");
  
        // make an object of the class gui
        gui f1 = new gui();
        
        // add action listener to the button to capture user
        // response on buttons
        button1.addActionListener(f1);
        button2.addActionListener(f1);
  
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ProcessImage();
            }
        });
        
        // make a panel to add the buttons and labels
        JPanel p = new JPanel();
  
        // add buttons to the frame
        p.add(button1);
        p.add(button2);
        p.add(button3);
        
     // set the label to its initial value
        l = new JLabel("no file selected");
  
        // add panel to the frame
        p.add(l);
        f.add(p);
  
        f.show();
	}
	
	public void actionPerformed(ActionEvent evt)
    {
        // if the user presses the save button show the save dialog
        String com = evt.getActionCommand();
        
        // create an object of JFileChooser class
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        // invoke the showsOpenDialog function to show the save dialog
        int r = j.showOpenDialog(null);
  
        if (com.equals("openTrainingData")) {
            if (r == JFileChooser.APPROVE_OPTION)
  
            {
            	File file = j.getSelectedFile();
                l.setText(file.getAbsolutePath());
                AddTrainingData(file);
            }
            else
                l.setText("the user cancelled the operation");
        }
  
        // if the user presses the open image dialog show the open dialog
        if (com.contentEquals("openImage")) {
            if (r == JFileChooser.APPROVE_OPTION)
            {
                File file = j.getSelectedFile();
                l.setText(file.getAbsolutePath());
                AddImage(file);
            }
            else
                l.setText("the user cancelled the operation");
        }
    }
	
	private void AddTrainingData(File file) {
		try (InputStream inputStream = new FileInputStream(file)) {
			
			//read all data into byte array
			long fileSize = file.length(); 
            byte[] allBytes = new byte[(int) fileSize];
            inputStream.read(allBytes);
            
            //split all bytes into each file 3073 bytes
            byte[][] allFilesAsBytes = divideArray(allBytes, 3073);
            
            //loop over each byte[] file and create a Train object
            for (int trainFileNumber = 0; trainFileNumber < allFilesAsBytes.length; trainFileNumber++) 
            { 
            	byte label = allFilesAsBytes[trainFileNumber][0];
            	String labelString = new String(new byte[]{ label });
            	byte[] image = Arrays.copyOfRange(allFilesAsBytes[trainFileNumber], 1, 3072);
            	
            	trainDataset.add(new Train(labelString, image));
            }

           
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	private void AddImage(File file) {
		try (InputStream inputStream = new FileInputStream(file)) {
			
			//read all data into byte array
			long fileSize = file.length(); 
            byte[] imageBytes = new byte[(int) fileSize];
            inputStream.read(imageBytes);
            
            //set image
            image = imageBytes;           
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	private static void ProcessImage() {
		// do something with the algorithm here
	}
	
	private static byte[][] divideArray(byte[] source, int chunksize) {


        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];

        int start = 0;

        for(int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source,start, start + chunksize);
            start += chunksize ;
        }

        return ret;
    }
}
      