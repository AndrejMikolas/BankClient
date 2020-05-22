package sk.andrejmik.bankclient.repository_interface;

import sk.andrejmik.bankclient.api_repository.ApiAccountRepository;

public class RepositoryFactory
{
    private static final RepositoryType mRepositoryType = RepositoryType.API;
    
    public static IAccountRepository getAccountRepository()
    {
        switch (mRepositoryType)
        {
            case API:
                return new ApiAccountRepository();
            default:
                return new ApiAccountRepository();
        }
    }
    
    enum RepositoryType
    {
        API
    }
}
