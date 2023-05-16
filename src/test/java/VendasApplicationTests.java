import github.jeanalvesr.VendasApplication;
import github.jeanalvesr.config.LocalConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = LocalConfig.class)
public class VendasApplicationTests {

    @Test
    void main(){
        VendasApplication.main(new String[]{});
    }
}
