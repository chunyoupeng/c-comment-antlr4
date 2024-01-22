import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.HashSet;
import java.util.Set;

public class SpecialCommentListener extends CBaseListener {

    String rocketStruct = 
	"\ntypedef struct {\n" +
	"    char name[50];          // 火箭的名称\n" +
	"    double height;          // 火箭的高度（米）\n" +
	"    double weight;          // 火箭的重量（千克）\n" +
	"    double thrust;          // 火箭的推力（牛顿）\n" +
	"    double fuelCapacity;    // 燃料容量（升）\n" +
	"    int numberOfEngines;    // 发动机数量\n" +
	"    char engineType[30];    // 发动机类型\n" +
	"    double maxAltitude;     // 最大飞行高度（米）\n" +
	"    char missionType[50];   // 任务类型（例如，载人、卫星发射等）\n" +
	"    bool isReusable;        // 是否可重复使用\n" +
	"} Rocket;\n";

    String missileStruct = 
	"\ntypedef struct {\n" +
	"    char name[50];         // 导弹的名称\n" +
	"    double length;         // 导弹的长度（米）\n" +
	"    double diameter;       // 导弹的直径（米）\n" +
	"    double weight;         // 导弹的重量（千克）\n" +
	"    double range;          // 射程（千米）\n" +
	"    double speed;          // 速度（马赫）\n" +
	"    char warheadType[30];  // 弹头类型\n" +
	"    double warheadWeight;  // 弹头重量（千克）\n" +
	"    char guidanceSystem[50]; // 导引系统\n" +
	"} Missile;\n";

    private TokenStreamRewriter rewriter;
    private Set<Token> processedTokens;

    public SpecialCommentListener(BufferedTokenStream tokens) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.processedTokens = new HashSet<>();
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        for (int i = 0; i < rewriter.getTokenStream().size(); i++) {
            Token token = rewriter.getTokenStream().get(i);
            if (token.getChannel() == Lexer.HIDDEN && token.getType() == CParser.SpecialComment) {
                // 检查是否已经处理过这个令牌
                if (!processedTokens.contains(token)) {
                    processedTokens.add(token);
                    String commentText = token.getText();
                    if (commentText.contains("racket")) {
                        // 在特殊注释前插入结构体定义
                        rewriter.insertAfter(token, rocketStruct);
                    }else if (commentText.contains("missile")) {
                // 在特殊注释前插入导弹结构体定义
                rewriter.insertAfter(token, missileStruct);
            }
                }
            }
        }
    }

    public String getResult() {
        return rewriter.getText();
    }
}
