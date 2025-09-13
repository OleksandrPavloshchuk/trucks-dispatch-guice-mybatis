package tutorial.trucksDispatchGuiceMybatis.services;

import tutorial.trucksDispatchGuiceMybatis.services.events.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.events.out.OutputEvent;

public interface Distributor {
    OutputEvent onTruckArrived(TruckArrivedInputEvent event);

    OutputEvent onShipmentArrived(ShipmentArrivedInputEvent event);
}
