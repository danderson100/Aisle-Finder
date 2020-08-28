import java.util.List;

public interface Save {
    List<String> write();

    void read(List<String> savedValues);
}


