package ua.nechay.lacon.ast;

import ua.nechay.lacon.core.LaconProgramState;
import ua.nechay.lacon.core.function.FunctionLaconValue;

import javax.annotation.Nonnull;

/**
 * @author anechaev
 * @since 16.06.2023
 */
public class ExportAST implements AST {

    private final AST exportableAST;

    public ExportAST(@Nonnull AST exportableAST) {
        this.exportableAST = exportableAST;
    }

    @Nonnull
    @Override
    public LaconProgramState interpret(@Nonnull LaconProgramState state) {
        if (!(exportableAST instanceof FunctionDeclarationAST)) {
            throw new IllegalStateException("Unable to export non-function AST!");
        }
        FunctionDeclarationAST functionDeclarationAST = (FunctionDeclarationAST) exportableAST;
        LaconProgramState stateAfterExportableInterpretation = functionDeclarationAST.interpret(state);
        String functionName = functionDeclarationAST.getFunctionName();
        FunctionLaconValue functionForExport = stateAfterExportableInterpretation.getFunction(functionName);
        if (functionForExport == null) {
            throw new IllegalStateException("Unable to export function " + functionName);
        }
        return stateAfterExportableInterpretation.putFunctionForExport(functionName, functionForExport);
    }
}