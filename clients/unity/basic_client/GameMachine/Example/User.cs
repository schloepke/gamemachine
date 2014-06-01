using GameMachine;

public sealed class User
{
       
    static readonly User _instance = new User();
    public static User Instance
    {
        get
        {
            return _instance;
        }
    }
        
    User()
    {
    }

    public string username;
    public string password;
        
    public void SetUser(string _username, string _password)
    {
        username = _username;
        password = _password;
    }

}
