package Controller.Visitor;

public interface Visitable {
    void accept(SavingVisitor visitor);
}
