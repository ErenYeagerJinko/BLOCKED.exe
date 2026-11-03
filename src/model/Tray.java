package model;

import exceptions.*;
import util.*;
import java.util.*;

public class Tray {
    public static final int SLOT_COUNT = 3;
    public static final int NO_SELECTION = -1;

    private BlockPiece[] slots;
    private int selectedSlotIndex;

    public Tray() {
        this.slots = new BlockPiece[SLOT_COUNT];
        this.selectedSlotIndex = NO_SELECTION;
        refill();
    }

    public void refill() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            try {
                slots[i] = PieceGenerator.getRandomPiece();
            } catch (InvalidPieceException e) {
                Logger.getLogger().error("Tray could not generate piece for slot " + i + ": " + e.getMessage());
                slots[i] = null;
            }
        }
        selectedSlotIndex = NO_SELECTION;
        Logger.getLogger().log("Tray refilled with " + getRemainingPieces().size() + " new pieces");
    }

    public BlockPiece getPiece(int slotIndex) {
        if (!isValidSlot(slotIndex)) {
            return null;
        }
        return slots[slotIndex];
    }

    // Removes a placed piece; refills the tray once all slots are used up
    public void consumePiece(int slotIndex) {
        if (!hasPiece(slotIndex)) {
            return;
        }
        slots[slotIndex] = null;
        if (selectedSlotIndex == slotIndex) {
            selectedSlotIndex = NO_SELECTION;
        }
        Logger.getLogger().log("Tray slot " + slotIndex + " consumed");

        if (isEmpty()) {
            refill();
        }
    }

    public boolean isEmpty() {
        for (BlockPiece piece : slots) {
            if (piece != null) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPiece(int slotIndex) {
        return isValidSlot(slotIndex) && slots[slotIndex] != null;
    }

    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }

    public void setSelectedSlotIndex(int index) {
        if (index == NO_SELECTION || hasPiece(index)) {
            this.selectedSlotIndex = index;
        }
    }

    public BlockPiece getSelectedPiece() {
        return getPiece(selectedSlotIndex);
    }

    public java.util.List<BlockPiece> getRemainingPieces() {
        java.util.List<BlockPiece> remaining = new ArrayList<>();
        for (BlockPiece piece : slots) {
            if (piece != null) {
                remaining.add(piece);
            }
        }
        return remaining;
    }

    public BlockPiece[] getSlots() {
        return Arrays.copyOf(slots, SLOT_COUNT);
    }

    public void setSlots(BlockPiece[] slots) {
        if (slots == null || slots.length != SLOT_COUNT) {
            return;
        }
        this.slots = Arrays.copyOf(slots, SLOT_COUNT);
        this.selectedSlotIndex = NO_SELECTION;
    }

    private boolean isValidSlot(int slotIndex) {
        return slotIndex >= 0 && slotIndex < SLOT_COUNT;
    }
}
