package me.travelplan.service.cart;

import lombok.RequiredArgsConstructor;
import me.travelplan.service.cart.exception.PlaceExistedException;
import me.travelplan.service.place.Place;
import me.travelplan.service.place.PlaceRepository;
import me.travelplan.service.place.exception.PlaceNotFoundException;
import me.travelplan.service.user.User;
import me.travelplan.web.cart.CartPlaceRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartPlaceService {
    private final PlaceRepository placeRepository;
    private final CartPlaceRepository cartPlaceRepository;

    @Transactional
    public void addPlace(CartPlaceRequest.AddPlace request, User user) {
        if (cartPlaceRepository.findByPlaceIdAndCreatedBy(request.getPlaceId(), user).isPresent()) {
            throw new PlaceExistedException();
        }
        Place place = placeRepository.findById(request.getPlaceId()).orElseThrow(PlaceNotFoundException::new);
        CartPlace cartPlace = CartPlace.create(place);
        cartPlaceRepository.save(cartPlace);
    }

    public List<CartPlace> getMyCart(User user) {
        return cartPlaceRepository.findAllByCreatedBy(user);
    }
}