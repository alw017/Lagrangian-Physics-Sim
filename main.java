import java.util.Scanner;
import com.PhysicsController;
public class main {
    public static void main(String[] args) {
        PhysicsController a = new PhysicsController(50, 0, 0.3d, 0, 0, 0);
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.println(a.toString());
            s.nextLine();
        }
    }
    
}
