// Generated from C:/Code/EzASM/src/main/java/com/ezasm/backend\GDBMI.g4 by ANTLR 4.10.1
package com.ezasm.antlr.backend.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GDBMIParser}.
 */
public interface GDBMIListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#output}.
	 * @param ctx the parse tree
	 */
	void enterOutput(GDBMIParser.OutputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#output}.
	 * @param ctx the parse tree
	 */
	void exitOutput(GDBMIParser.OutputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#result_record}.
	 * @param ctx the parse tree
	 */
	void enterResult_record(GDBMIParser.Result_recordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#result_record}.
	 * @param ctx the parse tree
	 */
	void exitResult_record(GDBMIParser.Result_recordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#out_of_band_record}.
	 * @param ctx the parse tree
	 */
	void enterOut_of_band_record(GDBMIParser.Out_of_band_recordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#out_of_band_record}.
	 * @param ctx the parse tree
	 */
	void exitOut_of_band_record(GDBMIParser.Out_of_band_recordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#async_record}.
	 * @param ctx the parse tree
	 */
	void enterAsync_record(GDBMIParser.Async_recordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#async_record}.
	 * @param ctx the parse tree
	 */
	void exitAsync_record(GDBMIParser.Async_recordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#exec_async_output}.
	 * @param ctx the parse tree
	 */
	void enterExec_async_output(GDBMIParser.Exec_async_outputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#exec_async_output}.
	 * @param ctx the parse tree
	 */
	void exitExec_async_output(GDBMIParser.Exec_async_outputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#status_async_output}.
	 * @param ctx the parse tree
	 */
	void enterStatus_async_output(GDBMIParser.Status_async_outputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#status_async_output}.
	 * @param ctx the parse tree
	 */
	void exitStatus_async_output(GDBMIParser.Status_async_outputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#notify_async_output}.
	 * @param ctx the parse tree
	 */
	void enterNotify_async_output(GDBMIParser.Notify_async_outputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#notify_async_output}.
	 * @param ctx the parse tree
	 */
	void exitNotify_async_output(GDBMIParser.Notify_async_outputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#async_output}.
	 * @param ctx the parse tree
	 */
	void enterAsync_output(GDBMIParser.Async_outputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#async_output}.
	 * @param ctx the parse tree
	 */
	void exitAsync_output(GDBMIParser.Async_outputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#result_class}.
	 * @param ctx the parse tree
	 */
	void enterResult_class(GDBMIParser.Result_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#result_class}.
	 * @param ctx the parse tree
	 */
	void exitResult_class(GDBMIParser.Result_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#async_class}.
	 * @param ctx the parse tree
	 */
	void enterAsync_class(GDBMIParser.Async_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#async_class}.
	 * @param ctx the parse tree
	 */
	void exitAsync_class(GDBMIParser.Async_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#result}.
	 * @param ctx the parse tree
	 */
	void enterResult(GDBMIParser.ResultContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#result}.
	 * @param ctx the parse tree
	 */
	void exitResult(GDBMIParser.ResultContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(GDBMIParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(GDBMIParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(GDBMIParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(GDBMIParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#tuple}.
	 * @param ctx the parse tree
	 */
	void enterTuple(GDBMIParser.TupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#tuple}.
	 * @param ctx the parse tree
	 */
	void exitTuple(GDBMIParser.TupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(GDBMIParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(GDBMIParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#stream_record}.
	 * @param ctx the parse tree
	 */
	void enterStream_record(GDBMIParser.Stream_recordContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#stream_record}.
	 * @param ctx the parse tree
	 */
	void exitStream_record(GDBMIParser.Stream_recordContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#console_stream_output}.
	 * @param ctx the parse tree
	 */
	void enterConsole_stream_output(GDBMIParser.Console_stream_outputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#console_stream_output}.
	 * @param ctx the parse tree
	 */
	void exitConsole_stream_output(GDBMIParser.Console_stream_outputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#target_stream_output}.
	 * @param ctx the parse tree
	 */
	void enterTarget_stream_output(GDBMIParser.Target_stream_outputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#target_stream_output}.
	 * @param ctx the parse tree
	 */
	void exitTarget_stream_output(GDBMIParser.Target_stream_outputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#log_stream_output}.
	 * @param ctx the parse tree
	 */
	void enterLog_stream_output(GDBMIParser.Log_stream_outputContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#log_stream_output}.
	 * @param ctx the parse tree
	 */
	void exitLog_stream_output(GDBMIParser.Log_stream_outputContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#nl}.
	 * @param ctx the parse tree
	 */
	void enterNl(GDBMIParser.NlContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#nl}.
	 * @param ctx the parse tree
	 */
	void exitNl(GDBMIParser.NlContext ctx);
	/**
	 * Enter a parse tree produced by {@link GDBMIParser#c_string}.
	 * @param ctx the parse tree
	 */
	void enterC_string(GDBMIParser.C_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link GDBMIParser#c_string}.
	 * @param ctx the parse tree
	 */
	void exitC_string(GDBMIParser.C_stringContext ctx);
}