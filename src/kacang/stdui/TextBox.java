package kacang.stdui;

/**
 * Represents an HTML textarea.
 */
public class TextBox extends TextField {
	private String onKeyPress;
    private String rows = "10";
    private String cols = "40";

    /**
     * Creates a new instance of TextBox
     */
    public TextBox() {
        super();
    }

    /**
     * Creates a new instance of TextBox with specific name
     */
    public TextBox(String name) {
        super(name);
    }

    /**
     * Creates a new instance of TextBox with specific name and value
     */
    public TextBox(String name, String value) {
        super(name);
        setValue(value);
    }

    public String getOnKeyPress() {
		return onKeyPress;
	}

	public void setOnKeyPress(String onKeyPress) {
		this.onKeyPress = onKeyPress;
	}

	/**
     * Retrieves the number of horizontal rows contained in the object
     * @return number of horizontal rows
     */
    public String getRows() {
        return rows;
    }

    /**
     * Sets the number of horizontal rows contained in the object
     * @param rows Number of horizontal rows
     */
    public void setRows(String rows) {
        this.rows = rows;
    }

    /**
     * Retrieves the width of the object
     * @return width of the TextBox
     */
    public String getCols() {
        return cols;
    }

    /**
     * Sets the width of the object
     * @param cols Width of the TextBox
     */
    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getDefaultTemplate() {
        return "textbox";
    }
}