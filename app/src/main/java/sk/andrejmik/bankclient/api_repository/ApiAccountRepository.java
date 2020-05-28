package sk.andrejmik.bankclient.api_repository;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sk.andrejmik.bankclient.objects.Account;
import sk.andrejmik.bankclient.repository_interface.IAccountRepository;
import sk.andrejmik.bankclient.utils.Globals;

/**
 * Remote API repository for Accounts
 */
public class ApiAccountRepository implements IAccountRepository
{
    /**
     * Get single Account observable by provided ID
     *
     * @param id ID of account to load from API
     *
     * @return Observable for account
     */
    @Override
    public Observable<Account> get(Object id)
    {
        String requestUrl = ApiRepositoryConstants.getServerAddress() + "/accounts/detail/" + id.toString();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(requestUrl).get().build();
        
        return Observable.create(new ObservableOnSubscribe<Account>()
        {
            @Override
            public void subscribe(final ObservableEmitter<Account> emitter)
            {
                client.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e)
                    {
                        emitter.onError(e);
                    }
                    
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                    {
                        if (response.isSuccessful())
                        {
                            String jsonBody = Objects.requireNonNull(response.body()).string();
                            Account result = Globals.GSON.fromJson(jsonBody, Account.class);
                            emitter.onNext(result);
                            emitter.onComplete();
                        }
                        else
                        {
                            emitter.onError(new IllegalStateException(String.valueOf(response.code())));
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
    
    /**
     * Get observable list of all accounts
     *
     * @return Observable list of all accounts
     */
    @Override
    public Observable<List<Account>> getAll()
    {
        String requestUrl = ApiRepositoryConstants.getServerAddress() + "/accounts/list";
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(requestUrl).get().build();
        
        return Observable.create(new ObservableOnSubscribe<List<Account>>()
        {
            @Override
            public void subscribe(final ObservableEmitter<List<Account>> emitter)
            {
                client.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e)
                    {
                        emitter.onError(e);
                    }
                    
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                    {
                        if (response.isSuccessful())
                        {
                            String jsonBody = Objects.requireNonNull(response.body()).string();
                            Type listType = new TypeToken<ArrayList<Account>>()
                            {
                            }.getType();
                            List<Account> result = Globals.GSON.fromJson(jsonBody, listType);
                            emitter.onNext(result);
                            emitter.onComplete();
                        }
                        else
                        {
                            emitter.onError(new IllegalStateException(String.valueOf(response.code())));
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
    
    /**
     * Save account to remote API and get it back with new ID
     *
     * @param data Account to save
     *
     * @return Saved account observable with generated ID
     */
    @Override
    public Observable<Account> save(Account data)
    {
        String requestUrl = ApiRepositoryConstants.getServerAddress() + "/accounts/save";
        final OkHttpClient client = new OkHttpClient();
        String jsonBody = Globals.GSON.toJson(data);
        RequestBody body = RequestBody.create(ApiRepositoryConstants.JSON, jsonBody);
        final Request request = new Request.Builder().url(requestUrl).post(body).build();
        return Observable.create(new ObservableOnSubscribe<Account>()
        {
            @Override
            public void subscribe(final ObservableEmitter<Account> emitter)
            {
                client.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e)
                    {
                        emitter.onError(e);
                    }
                    
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                    {
                        if (response.isSuccessful())
                        {
                            String jsonBody = Objects.requireNonNull(response.body()).string();
                            Account result = Globals.GSON.fromJson(jsonBody, Account.class);
                            emitter.onNext(result);
                            emitter.onComplete();
                        }
                        else
                        {
                            emitter.onError(new IllegalStateException(String.valueOf(response.code())));
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
    
    /**
     * Delete account in API as a observable
     *
     * @param id Account ID to delete
     *
     * @return Void observable
     */
    @Override
    public Observable<Void> delete(Object id)
    {
        String requestUrl = ApiRepositoryConstants.getServerAddress() + "/accounts/remove/" + id.toString();
        final OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(null, new byte[0]);
        final Request request = new Request.Builder().url(requestUrl).post(body).build();
        return Observable.create(new ObservableOnSubscribe<Void>()
        {
            @Override
            public void subscribe(final ObservableEmitter<Void> emitter)
            {
                client.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e)
                    {
                        emitter.onError(e);
                    }
                    
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response)
                    {
                        if (response.isSuccessful())
                        {
                            emitter.onComplete();
                        }
                        else
                        {
                            emitter.onError(new IllegalStateException(String.valueOf(response.code())));
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
}
