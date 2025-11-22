package btl_oop.btl_oop.Services;

import btl_oop.btl_oop.Repo.TypeSlotRepo;
import btl_oop.btl_oop.Repo.CourtRepo;
import btl_oop.btl_oop.Repo.SlotBookedRepo;
import btl_oop.btl_oop.Repo.SlotRepo;

import btl_oop.btl_oop.Models.Slot;
import btl_oop.btl_oop.Models.TypeSlots;
import btl_oop.btl_oop.Models.Court;
import btl_oop.btl_oop.Models.SlotBooked;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SlotService {

    private final SlotRepo slotRepo;
    private final TypeSlotRepo typeSlotRepo;
    private final CourtRepo courtRepo;
    private final SlotBookedRepo slotBookedRepo;

    public SlotService(
            SlotRepo slotRepo,
            TypeSlotRepo typeSlotRepo,
            CourtRepo courtRepo,
            SlotBookedRepo slotBookedRepo
    ) {
        this.slotRepo = slotRepo;
        this.typeSlotRepo = typeSlotRepo;
        this.courtRepo = courtRepo;
        this.slotBookedRepo = slotBookedRepo;
    }

    // ===================== SLOT =====================
    public List<Slot> getAllSlots() {
        return slotRepo.findAll();
    }

    public Optional<Slot> getSlotById(Long id) {
        return slotRepo.findById(id);
    }

    public Slot saveSlot(Slot slot) {
        return slotRepo.save(slot);
    }

    public void deleteSlot(Long id) {
        if (!slotRepo.existsById(id))
            throw new IllegalArgumentException("Slot không tồn tại");
        slotRepo.deleteById(id);
    }

    // **Xóa nhiều slot**
    public void deleteSlots(List<Long> ids) {
        List<Slot> slots = slotRepo.findAllById(ids);
        if (slots.size() != ids.size()) {
            throw new IllegalArgumentException("Một số slot không tồn tại");
        }
        slotRepo.deleteAll(slots);
    }

    // ===================== TYPE SLOT =====================
    public List<TypeSlots> getAllTypeSlots() {
        return typeSlotRepo.findAll();
    }

    public TypeSlots saveTypeSlot(TypeSlots typeSlot) {
        return typeSlotRepo.save(typeSlot);
    }

    // ===================== COURT =====================
    public List<Court> getAllCourts() {
        return courtRepo.findAll();
    }

    public Optional<Court> getCourtById(Long id) {
        return courtRepo.findById(id);
    }

    public Court saveCourt(Court court) {
        return courtRepo.save(court);
    }

    // ===================== SLOT BOOKED =====================
    public List<SlotBooked> getBookedSlotByDate(LocalDate date) {
        return slotBookedRepo.findByDate(date);
    }

    public List<SlotBooked> getBookingsByCourt(Long courtId) {
        return slotBookedRepo.findByCourtId(courtId);
    }

    // ===================== BOOKING LOGIC =====================
    public SlotBooked saveBookedSlot(SlotBooked booked) {

        Long courtId = booked.getCourt().getId();
        Long slotId = booked.getSlot().getId();
        LocalDate date = booked.getDate();

        if (!courtRepo.existsById(courtId)) {
            throw new IllegalArgumentException("Sân không tồn tại");
        }

        if (!slotRepo.existsById(slotId)) {
            throw new IllegalArgumentException("Khung giờ không tồn tại");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }

        boolean exists = slotBookedRepo.findByCourtIdAndDate(courtId, date).stream()
                .anyMatch(sb -> sb.getSlot().getId().equals(slotId));

        if (exists) {
            throw new IllegalStateException("Khung giờ đã được đặt");
        }

        return slotBookedRepo.save(booked);
    }

    // ===================== AVAILABLE SLOTS =====================
    public List<Slot> getAvailableSlots(Long courtId, LocalDate date) {

        if (!courtRepo.existsById(courtId)) {
            throw new IllegalArgumentException("Sân không tồn tại");
        }

        List<SlotBooked> booked = slotBookedRepo.findByCourtIdAndDate(courtId, date);
        List<Long> bookedSlotIds = booked.stream()
                .map(sb -> sb.getSlot().getId())
                .toList();

        return slotRepo.findAll().stream()
                .filter(slot -> !bookedSlotIds.contains(slot.getId()))
                .toList();
    }
}
