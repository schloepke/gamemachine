
public class Main {
public static void main(String [ ] args)
{
int s = 2;
int z = "AOC".hashCode() % 3000;
int e = "QDE".hashCode() % 3000;
for (int d = 0; d <= e; d++)
    s = (s ^ d) % z;
System.out.println(s);
}
}