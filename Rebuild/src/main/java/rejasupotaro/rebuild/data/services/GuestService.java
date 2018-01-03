package rejasupotaro.rebuild.data.services;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.data.GuestNameTable;
import rejasupotaro.rebuild.data.models.Guest;
import rejasupotaro.rebuild.jobs.UpdateGuestTask;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class GuestService {

    public Observable<ArrayList<Guest>> getList(List<String> guestNames) {
        return Observable.from(addMiyagawaIfNecessary(guestNames))
                .map(GuestNameTable::inquire)
                .map(name -> {
                    Guest guest = Guest.findByName(name);

                    if (Guest.isEmpty(guest)) {
                        guest = TwitterApiClient.getInstance().getUser(name);
                        if (!Guest.isEmpty(guest)) {
                            guest.save();
                        }
                    } else {
                        new UpdateGuestTask(guest.getName()).execute();
                    }

                    return guest;
                })
                .filter(guest -> !Guest.isEmpty(guest))
                .reduce(new ArrayList<>(), (Func2<ArrayList<Guest>, Guest, ArrayList<Guest>>) (guests, guest) -> {
                    guests.add(guest);
                    return guests;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<String> addMiyagawaIfNecessary(List<String> names) {
        if (names.contains("miyagawa")) {
            return names;
        }
        names.add("miyagawa");
        return names;
    }
}
