/* Generated by JTB 1.4.9 */
package org.apcdevpowered.vcpu32.asm.parser.visitor;

import org.apcdevpowered.vcpu32.asm.parser.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.<br>
 * In your "VoidArgu" visitors extend this class and override part or all of these methods.
 *
 * @param <A> - The user argument type
 */
public class DepthFirstVoidArguVisitor<A> implements IVoidArguVisitor<A> {


  /*
   * Base nodes classes visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link NodeChoice} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final NodeChoice n, final A argu) {
    n.choice.accept(this, argu);
    return;
  }

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final NodeList n, final A argu) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this, argu);
    }
    return;
  }

  /**
   * Visits a {@link NodeListOptional} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final NodeListOptional n, final A argu) {
    if (n.present()) {
      for (final Iterator<INode> e = n.elements(); e.hasNext();) {
        e.next().accept(this, argu);
        }
      return;
    } else
      return;
  }

  /**
   * Visits a {@link NodeOptional} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final NodeOptional n, final A argu) {
    if (n.present()) {
      n.node.accept(this, argu);
      return;
    } else
      return;
  }

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final NodeSequence n, final A argu) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this, argu);
    }
    return;
  }

  /**
   * Visits a {@link NodeTCF} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final NodeTCF n, @SuppressWarnings("unused") final A argu) {
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return;
  }

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final NodeToken n, @SuppressWarnings("unused") final A argu) {
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return;
  }

  /*
   * User grammar generated visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link AbstractSyntaxTree} node, whose children are the following :
   * <p>
   * nodeOptional -> ( StmList() )?<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final AbstractSyntaxTree n, final A argu) {
    // nodeOptional -> ( StmList() )?
    final NodeOptional n0 = n.nodeOptional;
    if (n0.present()) {
      n0.accept(this, argu);
    }
    // nodeToken -> <EOF>
    final NodeToken n1 = n.nodeToken;
    n1.accept(this, argu);
  }

  /**
   * Visits a {@link StmList} node, whose children are the following :
   * <p>
   * stm -> Stm()<br>
   * nodeOptional -> ( StmList() )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final StmList n, final A argu) {
    // stm -> Stm()
    final Stm n0 = n.stm;
    n0.accept(this, argu);
    // nodeOptional -> ( StmList() )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      n1.accept(this, argu);
    }
  }

  /**
   * Visits a {@link Stm} node, whose child is the following :
   * <p>
   * nodeChoice -> . %0 Insn()<br>
   * .......... .. | %1 LabelDef()<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final Stm n, final A argu) {
    // nodeChoice -> . %0 Insn()
    // .......... .. | %1 LabelDef()
    final NodeChoice nch = n.nodeChoice;
    final INode ich = nch.choice;
    switch (nch.which) {
      case 0:
        // %0 Insn()
        ich.accept(this, argu);
        break;
      case 1:
        // %1 LabelDef()
        ich.accept(this, argu);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link Insn} node, whose children are the following :
   * <p>
   * nodeToken -> <Opt><br>
   * nodeOptional -> ( ParList() )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final Insn n, final A argu) {
    // nodeToken -> <Opt>
    final NodeToken n0 = n.nodeToken;
    n0.accept(this, argu);
    // nodeOptional -> ( ParList() )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      n1.accept(this, argu);
    }
  }

  /**
   * Visits a {@link LabelDef} node, whose children are the following :
   * <p>
   * nodeToken -> <LabelBegin><br>
   * nodeToken1 -> <Label><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final LabelDef n, final A argu) {
    // nodeToken -> <LabelBegin>
    final NodeToken n0 = n.nodeToken;
    n0.accept(this, argu);
    // nodeToken1 -> <Label>
    final NodeToken n1 = n.nodeToken1;
    n1.accept(this, argu);
  }

  /**
   * Visits a {@link ParList} node, whose children are the following :
   * <p>
   * par -> Par()<br>
   * nodeOptional -> ( #0 <Comma> #1 ParList() )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final ParList n, final A argu) {
    // par -> Par()
    final Par n0 = n.par;
    n0.accept(this, argu);
    // nodeOptional -> ( #0 <Comma> #1 ParList() )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence seq = (NodeSequence) n1.node;
      // #0 <Comma>
      final INode seq1 = seq.elementAt(0);
      seq1.accept(this, argu);
      // #1 ParList()
      final INode seq2 = seq.elementAt(1);
      seq2.accept(this, argu);
    }
  }

  /**
   * Visits a {@link Par} node, whose child is the following :
   * <p>
   * nodeChoice -> . %0 <Dec><br>
   * .......... .. | %1 <Hex><br>
   * .......... .. | %2 <Oct><br>
   * .......... .. | %3 <Bin><br>
   * .......... .. | %4 <Reg><br>
   * .......... .. | %5 #0 <MemBegin><br>
   * .......... .. . .. #1 ( &0 <Reg><br>
   * .......... .. . .. .. | &1 <Dec><br>
   * .......... .. . .. .. | &2 <Hex><br>
   * .......... .. . .. .. | &3 <Oct><br>
   * .......... .. . .. .. | &4 <Bin> )<br>
   * .......... .. . .. #2 <MemEnd><br>
   * .......... .. | %6 <String><br>
   * .......... .. | %7 <Char><br>
   * .......... .. | %8 <Label><br>
   * .......... .. | %9 <Real><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  @Override
  public void visit(final Par n, final A argu) {
    // nodeChoice -> . %0 <Dec>
    // .......... .. | %1 <Hex>
    // .......... .. | %2 <Oct>
    // .......... .. | %3 <Bin>
    // .......... .. | %4 <Reg>
    // .......... .. | %5 #0 <MemBegin>
    // .......... .. . .. #1 ( &0 <Reg>
    // .......... .. . .. .. | &1 <Dec>
    // .......... .. . .. .. | &2 <Hex>
    // .......... .. . .. .. | &3 <Oct>
    // .......... .. . .. .. | &4 <Bin> )
    // .......... .. . .. #2 <MemEnd>
    // .......... .. | %6 <String>
    // .......... .. | %7 <Char>
    // .......... .. | %8 <Label>
    // .......... .. | %9 <Real>
    final NodeChoice nch = n.nodeChoice;
    final INode ich = nch.choice;
    switch (nch.which) {
      case 0:
        // %0 <Dec>
        ich.accept(this, argu);
        break;
      case 1:
        // %1 <Hex>
        ich.accept(this, argu);
        break;
      case 2:
        // %2 <Oct>
        ich.accept(this, argu);
        break;
      case 3:
        // %3 <Bin>
        ich.accept(this, argu);
        break;
      case 4:
        // %4 <Reg>
        ich.accept(this, argu);
        break;
      case 5:
        // %5 #0 <MemBegin>
        // .. #1 ( &0 <Reg>
        // .. .. | &1 <Dec>
        // .. .. | &2 <Hex>
        // .. .. | &3 <Oct>
        // .. .. | &4 <Bin> )
        // .. #2 <MemEnd>
        final NodeSequence seq = (NodeSequence) ich;
        // #0 <MemBegin>
        final INode seq1 = seq.elementAt(0);
        seq1.accept(this, argu);
        // #1 ( &0 <Reg>
        // .. | &1 <Dec>
        // .. | &2 <Hex>
        // .. | &3 <Oct>
        // .. | &4 <Bin> )
        final INode seq2 = seq.elementAt(1);
        final NodeChoice nch1 = (NodeChoice) seq2;
        final INode ich1 = nch1.choice;
        switch (nch1.which) {
          case 0:
            // &0 <Reg>
            ich1.accept(this, argu);
            break;
          case 1:
            // &1 <Dec>
            ich1.accept(this, argu);
            break;
          case 2:
            // &2 <Hex>
            ich1.accept(this, argu);
            break;
          case 3:
            // &3 <Oct>
            ich1.accept(this, argu);
            break;
          case 4:
            // &4 <Bin>
            ich1.accept(this, argu);
            break;
          default:
            // should not occur !!!
            break;
        }
        // #2 <MemEnd>
        final INode seq3 = seq.elementAt(2);
        seq3.accept(this, argu);
        break;
      case 6:
        // %6 <String>
        ich.accept(this, argu);
        break;
      case 7:
        // %7 <Char>
        ich.accept(this, argu);
        break;
      case 8:
        // %8 <Label>
        ich.accept(this, argu);
        break;
      case 9:
        // %9 <Real>
        ich.accept(this, argu);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

}
