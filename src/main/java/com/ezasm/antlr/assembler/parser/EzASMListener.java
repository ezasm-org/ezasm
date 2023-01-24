// Generated from C:/Code/EzASM/src/main/java/com/ezasm/assembler\EzASM.g4 by ANTLR 4.10.1
package com.ezasm.antlr.assembler.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EzASMParser}.
 */
public interface EzASMListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link EzASMParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(EzASMParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link EzASMParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(EzASMParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link EzASMParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(EzASMParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link EzASMParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(EzASMParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link EzASMParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(EzASMParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EzASMParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(EzASMParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EzASMParser#dereference}.
	 * @param ctx the parse tree
	 */
	void enterDereference(EzASMParser.DereferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link EzASMParser#dereference}.
	 * @param ctx the parse tree
	 */
	void exitDereference(EzASMParser.DereferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link EzASMParser#immediate}.
	 * @param ctx the parse tree
	 */
	void enterImmediate(EzASMParser.ImmediateContext ctx);
	/**
	 * Exit a parse tree produced by {@link EzASMParser#immediate}.
	 * @param ctx the parse tree
	 */
	void exitImmediate(EzASMParser.ImmediateContext ctx);
}