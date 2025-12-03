package btl_oop.btl_oop.Services;

import btl_oop.btl_oop.Models.*;
import btl_oop.btl_oop.Repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtSlotService {

    private final CourtRepository courtRepo;
    private final SlotRepository slotRepo;
    private final SlotBookedRepository slotBookedRepo;

    public CourtSlotService(CourtRepository courtRepo, SlotRepository slotRepo, SlotBookedRepository slotBookedRepo) {
        this.courtRepo = courtRepo;
        this.slotRepo = slotRepo;
        this.slotBookedRepo = slotBookedRepo;
    }

    public List<Court> getAllCourts() {
        return courtRepo.findAll();
    }

    public List<Slot> getAllSlots() {
        return slotRepo.findAll();
    }

    public Long getCourtAvailable(){
        return courtRepo.countByStatus("available");
    }

    public record SlotPriceDTO(Long courtId, String courtName, Long slotId, String timeFrame, BigDecimal price) {}
    public List<SlotPriceDTO> getSlotPrices(LocalDate date) {
        List<Court> courts = courtRepo.findAll();
        List<Slot> slots = slotRepo.findAll();
        List<SlotPriceDTO> slotPrices = new ArrayList<>();
        for (Court court : courts) {
            for (Slot slot : slots) {
                SlotPriceDTO slotPrice = new SlotPriceDTO(court.getId(), court.getName(), slot.getId(), slot.getName(), slot.getTypeSlots().getPrice());
                slotPrices.add(slotPrice);
            }
        }
        return slotPrices;
    }
    public record SlotBookedDTO(Long courId, Long slotId, BigDecimal price, String fullName, String phone){};

    public List<SlotBookedDTO> getSlotBooked(LocalDate date, boolean isAdmin) {
        List<SlotBooked> slotBooked = slotBookedRepo.findByBookingDate(date);
        List<SlotBookedDTO> slotBookedDTO = new ArrayList<>();
        
        for (SlotBooked sb : slotBooked) {
            User user = sb.getBill().getUser();

            if(isAdmin){
                SlotBookedDTO dto = new SlotBookedDTO(
                    sb.getCourt().getId(), 
                    sb.getSlot().getId(), 
                    sb.getPrice(), 
                    user.getFullName(),
                    user.getPhone()
                );
                slotBookedDTO.add(dto);
            }
            else{
                SlotBookedDTO dto = new SlotBookedDTO(
                    sb.getCourt().getId(), 
                    sb.getSlot().getId(), 
                    sb.getPrice(), 
                    "Đã đặt",
                    "123"
                );
                slotBookedDTO.add(dto);
            }
        }
        return slotBookedDTO; 
    }
    
}