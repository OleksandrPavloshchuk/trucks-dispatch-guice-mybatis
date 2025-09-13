package tutorial.trucksDispatchGuiceMybatis.services;

import tutorial.trucksDispatchGuiceMybatis.services.event.in.ShipmentArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.event.in.TruckArrivedInputEvent;
import tutorial.trucksDispatchGuiceMybatis.services.event.out.OutputEvent;

public interface Distributor {
    OutputEvent onTruckArrived(TruckArrivedInputEvent event);

    OutputEvent onShipmentArrived(ShipmentArrivedInputEvent event);
}
