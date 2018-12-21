package org.nieghborhoodbikeworks.nbw.ui.queue;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nieghborhoodbikeworks.nbw.R;

public class QueueFragment extends Fragment {

    private QueueViewModel mViewModel;

    public static QueueFragment newInstance() {
        return new QueueFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.queue_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        // TODO: Use the ViewModel
    }

//    /**
//     * A single link of a linked list
//     */
//    class MemberLink {
//        protected Member mMember;
//        protected MemberLink mNext;
//
//        public MemberLink(Member member, MemberLink next) {
//            mMember = member;
//            mNext = next;
//        }
//
//        public Member getMember() {
//            return mMember;
//        }
//        public MemberLink getNext() {
//            return mNext;
//        }
//        public void setNext(MemberLink ml) { mNext = ml; }
//
//    }
//
//    class MemberIterator implements Iterator<Member> {
//        protected MemberLink mCur;
//        protected MemberLink mPrev = null;
//        protected MemberLink mPrevPrev = null;
//
//        public MemberIterator(MemberLink firstLink) {
//            mCur = firstLink;
//        }
//
//        public boolean hasNext() {
//            return mCur != null;
//        }
//
//        public Member next() {
//            Member m = mCur.getMember();
//            mPrevPrev = mPrev;
//            mPrev = mCur;
//            mCur = mCur.getNext();
//            return m;
//        }
//
//        public void dequeue() {
//            if (mCur == null && (mPrevPrev == null || mPrevPrev.getNext() == null)) {
//                mPrev.mMember = null;
//                mCur = mPrev;
//            } else {
//                //at the head
//                if (mPrevPrev == null) {
//                    mPrev.setNext(mCur.mNext);
//                    mCur.setNext(mPrev);
//                    mPrev.mMember = mCur.mMember;
//                    mCur = mPrev;
//                    mPrev = null;
//                }
//            }
//        }
//    }
//
//    public class MemberList extends DataSetObservable implements Iterable<Member>, Collection<Member> {
//        protected ArrayList<DataSetObserver> registeredObservers = new ArrayList<>();
//        protected MemberLink mFirst;
//
//        public MemberList() {
//            mFirst = null;
//        }
//
//        //Creates a Linked List by making each array element a SquirrelLink and having them point to
//        // the element in the array that is immediately after it
//        MemberList(ArrayList<Member> members) {
//            for (int i = members.size() - 1; i >= 0; i--) {
//                enqueue(members.get(i));
//            }
//        }
//
//        /**
//         * Adds a member to the back of the queue.
//         * @param member The member to add to the list
//         * @return {this}, the updated object after adding the member to the back of the list.
//         */
//        public MemberList enqueue(Member member) {
//            mFirst = new MemberLink(member, mFirst);
//            // notify Observers;
//            return this;
//        }
//
//        /**
//         * Get the first Member in the queue
//         * @return first member when queue is not empty
//         * @throws NullPointerException when queue is empty
//         */
//        public Member getFirst() {
//            if (mFirst != null) {
//                return mFirst.getMember();
//            } else {
//                throw new NullPointerException();
//            }
//        }
//
//        /**
//         *
//         * @return The size of the list
//         */
//        @Override
//        public int size() {
//            int i = 0;
//            for (MemberLink c = mFirst; c != null; c = c.getNext()) {
//                i++;
//            }
//            if (i == 1) {
//                if (mFirst.mMember == null) {
//                    i--;
//                }
//            }
//            return i;
//        }
//
//        /**
//         *
//         * @return Whether or not the linked list is empty
//         */
//        @Override
//        public boolean isEmpty() {
//            if (mFirst == null) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        @NonNull
//        @Override
//        public Iterator<Member> iterator() {
//            return new MemberIterator(mFirst);
//        }
//
//        /**
//         *
//         * @param <T>
//         * @param ts
//         * @return Returns all node data (i.e. Members) in a list
//         */
//        @NonNull
//        @Override
//        @SuppressWarnings("unchecked")
//        public <T> T[] toArray(@NonNull T[] ts) {
//            int i = 0;
//            MemberIterator j = new MemberIterator(mFirst);
//            while (j.hasNext()) {
//                ts[i] = ((T) j.mCur.getMember());
//                j.next();
//                i++;
//            }
//            return ts;
//        }
//
//        @NonNull
//        @Override
//        public Object[] toArray() {
//            Object[] arr = new Object[size()];
//            int j = 0;
//            for (Iterator<Member> i = iterator(); i.hasNext(); j++) {
//                arr[j] = i.next();
//            }
//            return arr;
//        }
//
//        //Goes through the collection and adds each squirrel in the collection to the back of the queue
//        @Override
//        public boolean addAll(@NonNull Collection<? extends Member> collection) {
//            for (Member member : collection) {
//                this.enqueue(member);
//            }
//            return true;
//        }
//
//        //Reassigns head to point to null, making the rest of the links in the list inaccessible (i.e.
//        //they all go to garbage collection)
//        @Override
//        public void clear() {
//            this.mFirst = null;
//        }
}
