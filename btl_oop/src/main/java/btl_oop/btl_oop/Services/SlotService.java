package btl_oop.btl_oop.Services;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.*;

import org.springframework.stereotype.Service;


import btl_oop.btl_oop.Repositories.SlotBookedRepository;
import btl_oop.btl_oop.Repositories.SlotRepository;
import btl_oop.btl_oop.Repositories.TypeSlotRepository;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SlotService {
    private final SlotBookedRepository bookedRepo;
    private final SlotRepository slotRepo;
    private final TypeSlotRepository typeSlotRepo;
    
    public Set<Long> getBookedsByCourtAndDate(Long courtId, LocalDate date) {
        Set<Long> bookedSlots = bookedRepo
                .findByCourt_IdAndDate(courtId, date)
                .stream()
                .map(sb -> sb.getSlot().getId())
                .collect(Collectors.toSet());
        return bookedSlots;
    }
    public Long getPrice(@NonNull Long slotId){
        Long typeId = slotRepo.findById(slotId).get().getId();
        Long price = typeSlotRepo.findById(typeId).get().getId();
        return price;
    }
}
