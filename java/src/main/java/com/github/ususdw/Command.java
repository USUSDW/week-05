package com.github.ususdw;

import java.util.List;

public interface Command {
    String run(List<String> args);
}
