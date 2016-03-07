package markharder.koreanzombie.game;

public class Question {
    private String definition;
    private String answer;

    public Question(String definition, String answer) {
        this.definition = definition;
        this.answer = answer;
    }

    public String getDefinition() {
        return definition;
    }

    public String getAnswer() {
        return answer;
    }
}
