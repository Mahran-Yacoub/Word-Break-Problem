import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class GUI {

	/** These ArrayList and ObservableList is used to fill ListView By output */
	private ObservableList<String> list;
	private ArrayList<String> listItem = new ArrayList<>();

	private static int size;

	/** Array 2D of Dynamic Programming */
	private static boolean[][] segmented;

	/** The input word */
	@FXML
	private TextField word;

	/** Input Dictionary */
	@FXML
	private TextField dictionary;

	/** Output */
	@FXML
	private ListView<String> output;

	/** Flag , It can be segmented ot not */
	@FXML
	private Labeled trueOrFalse;

	/** A file that contains dictionary , if we choose to browse */
	private File dictionaryFile;

	/**
	 * Close The Main Stage
	 * 
	 * @param event
	 */
	@FXML
	void cancelAction(ActionEvent event) {

		Driver.mainStage.close();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Browsing A dictionary File if you choose to enter it by file , not by text
	 * Field
	 * 
	 * @param event
	 */
	@FXML
	void chooseAction(ActionEvent event) {

		FileChooser choose = new FileChooser();
		dictionaryFile = choose.showOpenDialog(null);
		if (dictionaryFile != null) {

			readFile(dictionaryFile);
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will read a dictionary file and return the content in one string
	 * that contains words seperated by space and but it in the dictionary text
	 * field
	 * 
	 * @param file
	 */
	private void readFile(File file) {

		String dictionary = "";

		try {

			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {

				dictionary += " " + scan.nextLine().trim();
			}

			scan.close();

		} catch (FileNotFoundException e) {

			ShowError error = new ShowError();
			error.showError("Enter correct Type of File / text file");
		}

		this.dictionary.setText(dictionary);

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to clear all input and initialize a dynamic array and
	 * size
	 * 
	 * @param event
	 */
	@FXML
	void clearAction(ActionEvent event) {

		try {

			word.clear();
			dictionary.clear();
			trueOrFalse.setText("");
			list.clear();
			size = 0;
			segmented = null;

		} catch (NullPointerException e) {

			System.out.println("ObservableList is Not initialize");
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@FXML
	void solveAction(ActionEvent event) {

		String word = this.word.getText().trim();
		String dictionary = this.dictionary.getText().trim();

		// Test if there input word
		if (word.isEmpty()) {

			ShowError error1 = new ShowError();
			error1.showError("Please Enter A Word");
			return;
		}

		// Test if there input Dictionary either by testField or Browsing
		if (dictionary.isEmpty()) {

			ShowError error1 = new ShowError();
			error1.showError("Please Enter A Dictionary");
			return;
		}

		// Read a dictionary as array and convert it to ArrayList
		String[] dic = dictionary.split(" ");
		ArrayList<String> dict = new ArrayList<String>();
		convertArrayToList(dict, dic);

		// Array2D + flagIsSegmented
		BreakProblemOutput outputDB = wordbreak(dict, word);

		// If it is not can be segmented , so ends
		if (!outputDB.isSegmented()) {

			// It can not be segmented
			trueOrFalse.setText("False");
			return;
		}

		// we will take the size of array , its n * n
		// characters * characters
		size = outputDB.getSegmented().length;

		// Array 2D of Dynamic Programming
		segmented = outputDB.getSegmented();

		// Clear , For If it not the first time Using Solve Button
		listItem.clear();

		pritn(dict, word, outputDB.getSegmented(), "", 0, word.length() - 1);

		// Set The output in ListView
		list = FXCollections.observableArrayList(listItem);
		trueOrFalse.setText("True");
		output.setItems(list);

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void convertArrayToList(ArrayList<String> dict, String[] dic) {

		for (int i = 0; i < dic.length; i++) {

			dict.add(dic[i]);
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("static-access")
	@FXML
	void tableDPAction(ActionEvent event) throws IOException {

		if (size != 0) {

			GridPane matrix = new GridPane();
			matrix.setGridLinesVisible(true);
			HBox indexI1D = new HBox();
			VBox index1D = new VBox();

			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {

					// Create a new TextField in each Iteration
					Label tf = new Label();
					tf.setFont(new Font(15));
					tf.setPrefHeight(50);
					tf.setPrefWidth(50);
					tf.setAlignment(Pos.CENTER);
					tf.setText(String.valueOf(segmented[y][x]));
					if (segmented[y][x]) {
						tf.setTextFill(Color.GREEN);
					} else {
						tf.setTextFill(Color.RED);

					}

					matrix.setRowIndex(tf, y);
					matrix.setColumnIndex(tf, x);
					matrix.getChildren().add(tf);

				}

				Label label1D = new Label("" + y);
				label1D.setFont(new Font(20));
				label1D.setPrefHeight(50);
				label1D.setPrefWidth(50);
				label1D.setAlignment(Pos.CENTER);
				index1D.getChildren().add(label1D);

				Label labelI1D = new Label("  " + y);
				labelI1D.setFont(new Font(20));
				labelI1D.setPrefHeight(50);
				labelI1D.setPrefWidth(52);
				indexI1D.getChildren().add(labelI1D);
			}

			VBox column = new VBox();
			Label corner = new Label("i\\j");
			corner.setFont(new Font(20));
			corner.setAlignment(Pos.CENTER);
			corner.setPrefHeight(50);
			corner.setPrefWidth(50);
			column.getChildren().addAll(corner, index1D);

			VBox column2 = new VBox();
			column2.getChildren().addAll(indexI1D, matrix);

			HBox finalView = new HBox();
			finalView.getChildren().addAll(column, column2);

			Scene matrixScene = new Scene(finalView);
			Driver.tableStage.setTitle("Table of Dynamic Programming");
			Driver.tableStage.setScene(matrixScene);
			Driver.tableStage.show();

		} else {

			ShowError error = new ShowError();
			error.showError("You Need To Check if it can\nbe segmented in the first");

		}

	}

	/**
	 * This method will work on Dynamic Programming To Determine If A word can be
	 * segmented using a given Dictionary or Not.
	 * 
	 * @param dictionary Dictionary of Words
	 * @param word       A word that will be checked
	 * 
	 * @return An Object Contains DB_Array and flag if it can be segmented or not
	 * 
	 */
	public BreakProblemOutput wordbreak(List<String> dictionary, String word) {

		// I will depend on it to determine size of square matrix
		int length = word.length();

		/*
		 * 2D_Array will be DB array of this probelm It will store a solution of
		 * sub_Problems each parts of this words if it can be segmented or not by given
		 * dictionary
		 */
		boolean[][] segmented = new boolean[length][length];

		/*
		 * In This Loop I will fill a Diagonal check each character if it is can be
		 * segmented alone from a dictionary or if it is in dictionary or not.
		 * 
		 * initial case (i=j)--> S[i,j] = {true : s[i,j] is in dictionary , flase :
		 * s[i,j is not in dictionary}
		 * 
		 */
		for (int i = 0; i < segmented.length; i++) {

			if (dictionary.contains(word.charAt(i) + "")) {

				segmented[i][i] = true;

			}
		}

		/*
		 * Case2 : i!=j --> more then one character
		 * 
		 * The Table will fill Diagnol By Diagonal ,so we will loop # Diagonal -1 (we
		 * make one iteration alrady when i=j) and each iteration we will fill one
		 * Diagonal From larger to smaller
		 * 
		 * nested loop : loop for pass on diagonal , loop for pass elemets of each
		 * diagonal , loop to pass each elemte .
		 * 
		 * 
		 */
		for (int diagonal = 1; diagonal < length; diagonal++) {

			for (int i = 0; i < length - diagonal; i++) {

				int j = i + diagonal;

				for (int k = i; k < j; k++) {

					// Dynamic Prograaming , You will find the subproblem that repeate already
					// solved a storing
					boolean isSegmented = segmented[i][k] && segmented[k + 1][j];

					if (isSegmented) {

						// We store a solution of a subproblem , to use it when repeate
						segmented[i][j] = true;
					}

				}

				// i != j -- > i<=k<j , you dont check if a subword from i to j as one part is
				// can be segmented or not
				if (dictionary.contains(word.substring(i, j + 1))) {

					segmented[i][j] = true;

				}

			}

		}

		// Output Object contains Array_2D and The flag (The last element in an array ,
		// Top Rigth corner)
		BreakProblemOutput output = new BreakProblemOutput(segmented, segmented[0][length - 1]);
		return output;

	}

	/**
	 * This method will depend of Array_2D_DB to print all possible case to build a
	 * given word from a a given Dictionary.
	 * 
	 * @param dictionary : Given Dictionary of Strings
	 * 
	 * @param prefix     : A word that you want to find all possibile ways to
	 *                   degmented it each time.
	 * 
	 * @param segmented  : DP_Array That will store subproblems ,s o we will not
	 *                   repeate solve this problems.
	 * 
	 * @param out        : A String that will be have an output at the end.
	 * 
	 * @param from       index of start of prefix
	 * 
	 * @param to         index of end of prefix
	 */
	public void pritn(List<String> dictionary, String prefix, boolean[][] segmented, String out, int from, int to) {

		if (from > to) {

			listItem.add(out);
		}

		// Loop to pass thrrough all parts of prefix
		for (int k = from; k <= to; k++) {

			// if a first part of prefix can be segmented , then the second part can
			// defenitly segmented
			// , so we recursion a method

			if (segmented[from][k]) {

				String substring = prefix.substring(from, k + 1);
				if (dictionary.contains(substring)) {

					pritn(dictionary, prefix, segmented, out + " " + substring, k + 1, to);

				}
			}
		}

	}

}
