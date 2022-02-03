import dataview.models.*;

public class T10 extends Task{
	/*
	 * The constructor will decide how many inputports and how many outputports and the detailed information of each port.
	 */
	
	public T10()
	{
		super("T10", "tenth task in dummy workflow");
		ins = new InputPort[1];
		outs = new OutputPort[1];
		ins[0] = new InputPort("in0", Port.DATAVIEW_int, "This is the first number");
		//ins[1] = new InputPort("in1", Port.DATAVIEW_int, "This is the second number");
		outs[0] = new OutputPort("out0", Port.DATAVIEW_int, "This is the output");	
		//outs[1] = new OutputPort("out1", Port.DATAVIEW_int, "This is the output");	
	}
	
	
	
	public void run()
	{
		
	}
}
