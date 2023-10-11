package org.elasticsearch.experiments;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.elasticsearch.common.Version;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PoisonPillCheck extends AbstractCheck {
    @Override
    public int[] getDefaultTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getAcceptableTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[] { TokenTypes.METHOD_CALL };
    }

    Set<String> getIdentifiers(DetailAST ast, int tokenType) {
        Set<String> identifiers = new HashSet<>();
        for(DetailAST node = ast.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getType() == tokenType) {
                identifiers.add(node.getText());
            }
        }
        return identifiers;
    }

    List<String> getParameters(DetailAST ast) {
        if (ast == null) {
            return List.of();
        }

        if (ast.hasChildren() == false) {
            if (ast.getType() == TokenTypes.STRING_LITERAL) {
                return List.of(ast.getText());
            }
            return List.of();
        }

        List<String> parameters = new ArrayList<>();
        for(DetailAST node = ast.getFirstChild(); node != null; node = node.getNextSibling()) {
            parameters.addAll(getParameters(node));
        }
        return parameters;
    }

    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST dotAst = ast.findFirstToken(TokenTypes.DOT);
        if (dotAst == null) {
            return;
        }

        var identifiers = getIdentifiers(dotAst, TokenTypes.IDENT);
        if (identifiers.contains("checkPoisonPill") == false) {
            return;
        }

        List<String> parameters = getParameters(ast.findFirstToken(TokenTypes.ELIST));
        if (parameters.size() >= 2) {
            try {
                var message = parameters.get(1);
                var version = Integer.parseInt(parameters.get(0).replaceAll("\"", ""));

                if (version <= Version.CURRENT.getMajor()) {
                    log(ast, message);
                }
            }
            catch (NumberFormatException ignored) {

            }
        }
    }
}
