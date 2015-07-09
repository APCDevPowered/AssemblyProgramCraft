package org.apcdevpowered.vcpu32.asm;

import org.apcdevpowered.vcpu32.asm.DatatypeManager.Datatype;
import org.apcdevpowered.vcpu32.asm.DatatypeManager.ImageFormatException;
import org.apcdevpowered.vcpu32.asm.FragmentProgram.LabelConflictException;
import org.apcdevpowered.vcpu32.asm.OperatorsManager.IncmpParException;
import org.apcdevpowered.vcpu32.asm.OperatorsManager.ParImageFormatException;

public class CompileException extends RuntimeException
{
    private static final long serialVersionUID = -6388625872870770936L;
    public static final int UNKNOWN = 0;
    public static final int LABEL_CONFLICT = 1;
    public static final int INCMP_PAR_DATATYPE = 2;
    public static final int WRONG_PAR_IMAGE_FORMAT = 3;
    public final int type;
    private int lineNumber;
    
    public CompileException()
    {
        this.type = UNKNOWN;
    }
    public CompileException(int type)
    {
        this.type = type;
    }
    public CompileException(int type, int lineNumber)
    {
        this.type = type;
        this.lineNumber = lineNumber;
    }
    public CompileException(int type, int lineNumber, Throwable e)
    {
        super(e);
        this.type = type;
        this.lineNumber = lineNumber;
    }
    public CompileException(int type, Throwable e)
    {
        super(e);
        this.type = type;
    }
    @Override
    public String getMessage()
    {
        switch (type)
        {
            case UNKNOWN:
            {
                return "Unknown error occurred during complete.";
            }
            case LABEL_CONFLICT:
            {
                StringBuilder builder = new StringBuilder();
                builder.append("Label conflict error occurred during complete.");
                if (lineNumber > 0)
                {
                    builder.append("\n\tat line: ");
                    builder.append(lineNumber);
                }
                if (getCause() != null)
                {
                    LabelConflictException e = (LabelConflictException) getCause();
                    builder.append("\n\tLabel \"");
                    builder.append(e.getLabel());
                    builder.append('"');
                    builder.append(" confilict with other at line ");
                    builder.append(e.getLineNumber());
                }
                return builder.toString();
            }
            case INCMP_PAR_DATATYPE:
            {
                StringBuilder builder = new StringBuilder();
                builder.append("Incompatible parameter datatype error occurred during complete.");
                if (lineNumber > 0)
                {
                    builder.append("\n\tat line: ");
                    builder.append(lineNumber);
                }
                if (getCause() != null)
                {
                    IncmpParException e = (IncmpParException) getCause();
                    builder.append("\n\tParameter ");
                    builder.append(e.getParIndex());
                    builder.append(" with datatype <");
                    if (e.getDatatype() == null)
                    {
                        builder.append(DatatypeManager.typeVoid.getTypeName());
                    }
                    else
                    {
                        builder.append(e.getDatatype().getTypeName());
                    }
                    builder.append('>');
                    if (e.getCmpParDatatypes() == null)
                    {
                        builder.append(" must be datatype ");
                        builder.append("<");
                        builder.append(DatatypeManager.typeVoid.getTypeName());
                        builder.append('>');
                    }
                    else
                    {
                        builder.append(" must be datatype ");
                        boolean isFirstDataType = true;
                        for (Datatype<?> datatype : e.getCmpParDatatypes().getDatatypes())
                        {
                            if (!isFirstDataType)
                            {
                                builder.append(", ");
                            }
                            else
                            {
                                isFirstDataType = false;
                            }
                            builder.append("<");
                            builder.append(datatype.getTypeName());
                            builder.append('>');
                        }
                    }
                }
                return builder.toString();
            }
            case WRONG_PAR_IMAGE_FORMAT:
            {
                StringBuilder builder = new StringBuilder();
                builder.append("Wrong parameter image error occurred during complete.");
                if (lineNumber > 0)
                {
                    builder.append("\n\tat line: ");
                    builder.append(lineNumber);
                }
                if (getCause() != null)
                {
                    ParImageFormatException e1 = (ParImageFormatException) getCause();
                    builder.append("\n\tParameter ");
                    builder.append(e1.getParIndex());
                    builder.append(" with datatype <");
                    if (e1.getDatatype() == null)
                    {
                        builder.append(DatatypeManager.typeVoid.getTypeName());
                    }
                    else
                    {
                        builder.append(e1.getDatatype().getTypeName());
                    }
                    builder.append('>');
                    if (e1.getCause() != null)
                    {
                        ImageFormatException e2 = (ImageFormatException) e1.getCause();
                        builder.append("\n\t");
                        builder.append(e2.getMessage());
                    }
                }
                return builder.toString();
            }
            default:
                return "Unrecognized error occurred during complete";
        }
    }
    public int getLineNumber()
    {
        return lineNumber;
    }
}