package utils;


public interface SectionStateChangeListener {
    void onSectionStateChanged(Section section, boolean isOpen,int position);
}