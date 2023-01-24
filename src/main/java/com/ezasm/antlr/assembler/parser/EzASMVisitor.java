// Generated from C:/Code/EzASM/src/main/java/com/ezasm/assembler\EzASM.g4 by ANTLR 4.10.1
package com.ezasm.antlr.assembler.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link EzASMParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface EzASMVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link EzASMParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(EzASMParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link EzASMParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(EzASMParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link EzASMParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(EzASMParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link EzASMParser#dereference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDereference(EzASMParser.DereferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link EzASMParser#immediate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmediate(EzASMParser.ImmediateContext ctx);
}