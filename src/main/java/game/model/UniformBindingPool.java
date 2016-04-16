package game.model;

import java.util.Set;

import com.google.common.collect.Sets;

public class UniformBindingPool {

	Set<Integer> freeBindings = Sets.newHashSet();
	int          maxBinding;
	
	public class UniformBinding {
		private int bindingIndex;
		
		UniformBinding(int bindingIndex) {
			this.bindingIndex = bindingIndex;
		}
		
		protected void finalize() {
			if ( bindingIndex != -1 ) {
				removeBinding(this);
			}
		}
		
		public int getBindingIndex() {
			return bindingIndex;
		}
	}
	
	private void removeBinding(UniformBinding binding) {
		freeBindings.add(binding.bindingIndex);
	}
	
	public UniformBinding createBinding() {
		if ( freeBindings.isEmpty() ) {
			return new UniformBinding(maxBinding++);
		}
		else {
			Integer binding = freeBindings.iterator().next();
			freeBindings.remove(binding);
			return new UniformBinding(binding);
		}
	}
}
