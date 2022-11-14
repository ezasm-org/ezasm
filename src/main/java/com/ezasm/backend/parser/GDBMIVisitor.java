// Generated from C:/Code/EzASM/src/main/java/com/ezasm/backend\GDBMI.g4 by ANTLR 4.10.1
package com.ezasm.backend.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GDBMIParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GDBMIVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput(GDBMIParser.OutputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#result_record}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_record(GDBMIParser.Result_recordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#out_of_band_record}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut_of_band_record(GDBMIParser.Out_of_band_recordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#async_record}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsync_record(GDBMIParser.Async_recordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#exec_async_output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExec_async_output(GDBMIParser.Exec_async_outputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#status_async_output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatus_async_output(GDBMIParser.Status_async_outputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#notify_async_output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotify_async_output(GDBMIParser.Notify_async_outputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#async_output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsync_output(GDBMIParser.Async_outputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#result_class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_class(GDBMIParser.Result_classContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#async_class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsync_class(GDBMIParser.Async_classContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult(GDBMIParser.ResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(GDBMIParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(GDBMIParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#tuple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTuple(GDBMIParser.TupleContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(GDBMIParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#stream_record}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStream_record(GDBMIParser.Stream_recordContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#console_stream_output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConsole_stream_output(GDBMIParser.Console_stream_outputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#target_stream_output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_stream_output(GDBMIParser.Target_stream_outputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#log_stream_output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLog_stream_output(GDBMIParser.Log_stream_outputContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#nl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNl(GDBMIParser.NlContext ctx);
	/**
	 * Visit a parse tree produced by {@link GDBMIParser#c_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC_string(GDBMIParser.C_stringContext ctx);
}