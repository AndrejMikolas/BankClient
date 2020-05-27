package sk.andrejmik.bankclient.api_repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

public class ApiAccountRepository implements IAccountRepository
{
    private static final String API_URL = "http://192.168.1.20:8080";
//private static final String API_URL = "http://192.168.0.13:8080";
//private static final String API_URL = "http://192.168.8.101:8080";
    
    @Override
    public Observable<Account> get(Object id)
    {
        String requestUrl = API_URL + "/accounts/detail/" + id.toString();
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
                            emitter.onError(new IllegalStateException("${response.code}"));
                        }
                    }
                });
            }
        }).timeout(10, TimeUnit.SECONDS).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
    
    @Override
    public Observable<List<Account>> getAll()
    {
        String requestUrl = API_URL + "/accounts/list";
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
                            emitter.onError(new IllegalStateException("${response.code}"));
                        }
                    }
                });
            }
        }).timeout(10, TimeUnit.SECONDS).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
    
    @Override
    public Observable<Account> save(Account data)
    {
        String requestUrl = API_URL + "/accounts/save";
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
                            emitter.onError(new IllegalStateException("${response.code}"));
                        }
                    }
                });
            }
        }).timeout(30, TimeUnit.SECONDS).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
    
    @Override
    public void delete(Object id)
    {
    
    }
}
