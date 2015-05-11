package yuxuanchiadm.apc.vcpu32.asm.visitor;

import java.util.ArrayList;
import java.util.List;

import yuxuanchiadm.apc.vcpu32.asm.CompileException;
import yuxuanchiadm.apc.vcpu32.asm.DatatypeManager;
import yuxuanchiadm.apc.vcpu32.asm.FragmentProgram;
import yuxuanchiadm.apc.vcpu32.asm.OperatorsManager;
import yuxuanchiadm.apc.vcpu32.asm.Assembler.CompileContext;
import yuxuanchiadm.apc.vcpu32.asm.DatatypeManager.Datatype;
import yuxuanchiadm.apc.vcpu32.asm.FragmentProgram.LabelConflictException;
import yuxuanchiadm.apc.vcpu32.asm.FragmentProgram.MergeException;
import yuxuanchiadm.apc.vcpu32.asm.OperatorsManager.IncmpParException;
import yuxuanchiadm.apc.vcpu32.asm.OperatorsManager.Operator;
import yuxuanchiadm.apc.vcpu32.asm.OperatorsManager.ParImageFormatException;
import yuxuanchiadm.apc.vcpu32.asm.parser.VCPU32ParserConstants;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.AbstractSyntaxTree;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.Insn;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.LabelDef;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.NodeChoice;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.NodeList;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.NodeListOptional;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.NodeOptional;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.NodeSequence;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.NodeTCF;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.NodeToken;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.Par;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.ParList;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.Stm;
import yuxuanchiadm.apc.vcpu32.asm.parser.syntaxtree.StmList;
import yuxuanchiadm.apc.vcpu32.asm.parser.visitor.IRetArguVisitor;

public class CompileVisitor implements IRetArguVisitor<FragmentProgram, CompileContext>, VCPU32ParserConstants
{
    @Override
    public FragmentProgram visit(NodeChoice n, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(NodeList n, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(NodeListOptional n, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(NodeOptional n, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(NodeSequence n, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(NodeTCF n, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(NodeToken n, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(AbstractSyntaxTree abstractSyntaxTree, CompileContext context)
    {
        FragmentProgram program = null;
        if(abstractSyntaxTree.nodeOptional.present())
        {
            program = abstractSyntaxTree.nodeOptional.node.accept(this, context);
        }
        else
        {
            program = new FragmentProgram();
        }
        return program;
    }
    @Override
    public FragmentProgram visit(StmList stmList, CompileContext context)
    {
        FragmentProgram program = stmList.stm.accept(this, context);
        try
        {
            if(stmList.nodeOptional.present())
            {
                program.merge(stmList.nodeOptional.node.accept(this, context));
            }
        }
        catch (MergeException e)
        {
            switch (e.type)
            {
                case MergeException.LABEL_CONFLICT:
                    if(e.getCause() != null)
                    {
                        throw new CompileException(CompileException.LABEL_CONFLICT, e.getLineNumber(), e.getCause());
                    }
                    else
                    {
                        throw new CompileException(CompileException.LABEL_CONFLICT, e.getLineNumber());
                    }
                default:
                    throw new CompileException(CompileException.UNKNOWN);
            }
        }
        return program;
    }
    @Override
    public FragmentProgram visit(Stm stm, CompileContext context)
    {
        FragmentProgram program = null;
        switch (stm.nodeChoice.which)
        {
            //Insn
            case 0:
                program = stm.nodeChoice.choice.accept(this, context);
                break;
            //LabelDef
            case 1:
                program = stm.nodeChoice.choice.accept(this, context);
                break;
            default:
                throw new CompileException(CompileException.UNKNOWN);
        }
        return program;
    }
    @Override
    public FragmentProgram visit(Insn insn, CompileContext context)
    {
        FragmentProgram program = new FragmentProgram();
        Operator operator = OperatorsManager.fromImage(insn.nodeToken.tokenImage);
        int lineNumber = insn.nodeToken.beginLine;
        List<Datatype<?>> datatypes = new ArrayList<Datatype<?>>();
        List<String> parImages = new ArrayList<String>();
        if(insn.nodeOptional != null)
        {
            ParList parList = (ParList)insn.nodeOptional.node;
            while(parList != null)
            {
                Par par = parList.par;
                switch (par.nodeChoice.which)
                {
                    //Dec
                    case 0:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeDec);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Hex
                    case 1:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeHex);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Oct
                    case 2:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeOct);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Bin
                    case 3:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeBin);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Reg
                    case 4:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeReg);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Mem
                    case 5:
                        NodeSequence sequence = (NodeSequence)par.nodeChoice.choice;
                        NodeChoice choice = (NodeChoice)sequence.nodes.get(1);
                        switch (choice.which)
                        {
                            //Reg
                            case 0:
                            {
                                NodeToken token = (NodeToken)choice.choice;
                                datatypes.add(DatatypeManager.typeMemReg);
                                parImages.add("[" + token.tokenImage + "]");
                                break;
                            }
                            //Dec
                            case 1:
                            {
                                NodeToken token = (NodeToken)choice.choice;
                                datatypes.add(DatatypeManager.typeMemDec);
                                parImages.add("[" + token.tokenImage + "]");
                                break;
                            }
                            //Hex
                            case 2:
                            {
                                NodeToken token = (NodeToken)choice.choice;
                                datatypes.add(DatatypeManager.typeMemHex);
                                parImages.add("[" + token.tokenImage + "]");
                                break;
                            }
                            //Oct
                            case 3:
                            {
                                NodeToken token = (NodeToken)choice.choice;
                                datatypes.add(DatatypeManager.typeMemOct);
                                parImages.add("[" + token.tokenImage + "]");
                                break;
                            }
                            //Bin
                            case 4:
                            {
                                NodeToken token = (NodeToken)choice.choice;
                                datatypes.add(DatatypeManager.typeMemBin);
                                parImages.add("[" + token.tokenImage + "]");
                                break;
                            }
                            default:
                                throw new CompileException(CompileException.UNKNOWN);
                        }
                        break;
                    //String
                    case 6:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeString);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Char
                    case 7:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeChar);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Label
                    case 8:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeLabel);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    //Real
                    case 9:
                    {
                        NodeToken token = (NodeToken)par.nodeChoice.choice;
                        datatypes.add(DatatypeManager.typeReal);
                        parImages.add(token.tokenImage);
                        break;
                    }
                    default:
                        throw new CompileException(CompileException.UNKNOWN);
                }
                if(parList.nodeOptional.node != null)
                {
                    parList = (ParList)((NodeSequence)parList.nodeOptional.node).nodes.get(1);
                }
                else
                {
                    parList = null;
                }
            }
        }
        try
        {
            operator.checkParDatatypes(datatypes);
        }
        catch (IncmpParException e)
        {
            throw new CompileException(CompileException.INCMP_PAR_DATATYPE, insn.nodeToken.beginLine, e);
        }
        try
        {
            operator.addToProgram(program, lineNumber, datatypes, parImages, context);
        }
        catch (ParImageFormatException e)
        {
            throw new CompileException(CompileException.WRONG_PAR_IMAGE_FORMAT, insn.nodeToken.beginLine, e);
        }
        return program;
    }
    @Override
    public FragmentProgram visit(LabelDef labelDef, CompileContext context)
    {
        FragmentProgram program = new FragmentProgram();
        String label = labelDef.nodeToken1.tokenImage;
        program.getDebugInfo().addLabelLineNumber(label, labelDef.nodeToken.beginLine);
        program.getDebugInfo().addLabelOffset(label, 0);
        try
        {
            program.addLabel(label, 0);
        }
        catch (LabelConflictException e)
        {
            throw new CompileException(CompileException.LABEL_CONFLICT, labelDef.nodeToken.beginLine, e);
        }
        return program;
    }    
    @Override
    public FragmentProgram visit(ParList parList, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
    @Override
    public FragmentProgram visit(Par par, CompileContext context)
    {
        throw new UnsupportedOperationException();
    }
}